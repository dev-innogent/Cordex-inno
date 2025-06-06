import os
import re
import subprocess
from typing import Dict, List, Optional

import requests

try:
    import openai
except ImportError:  # pragma: no cover - optional dependency
    openai = None


def fetch_pr_details(repo: str, pr_number: str, token: str) -> Dict:
    """Fetch pull request details from GitHub."""
    url = f"https://api.github.com/repos/{repo}/pulls/{pr_number}"
    headers = {"Authorization": f"token {token}"}
    response = requests.get(url, headers=headers, timeout=10)
    response.raise_for_status()
    return response.json()


def fetch_reviews(repo: str, pr_number: str, token: str) -> List[Dict]:
    """Fetch pull request reviews from GitHub."""
    url = f"https://api.github.com/repos/{repo}/pulls/{pr_number}/reviews"
    headers = {"Authorization": f"token {token}"}
    response = requests.get(url, headers=headers, timeout=10)
    response.raise_for_status()
    return response.json()


def run_tests() -> str:
    """Run Maven tests and return the raw output."""
    result = subprocess.run(["mvn", "-q", "test"], capture_output=True, text=True)
    return result.stdout + "\n" + result.stderr


def parse_test_summary(output: str) -> str:
    """Extract a short summary from Maven test output."""
    match = re.search(r"Tests run: .*", output)
    return match.group(0) if match else "Test summary unavailable"


def generate_summary(api_key: str, pr: Dict, reviews: List[Dict], test_summary: str) -> str:
    """Use OpenAI to generate a short summary message."""
    if openai is None:
        raise RuntimeError("openai package not installed")
    openai.api_key = api_key
    review_states = {r['state'] for r in reviews}
    prompt = (
        f"PR Title: {pr['title']}\n"
        f"PR Body: {pr.get('body', '')}\n"
        f"Review States: {', '.join(review_states)}\n"
        f"Test Results: {test_summary}\n"
        "Summarize this information for the team in a short paragraph."
    )
    response = openai.ChatCompletion.create(
        model="gpt-3.5-turbo",
        messages=[{"role": "user", "content": prompt}],
        max_tokens=150,
    )
    return response.choices[0].message.content.strip()


def post_to_slack(webhook_url: str, message: str) -> None:
    """Send the given message to Slack via webhook."""
    payload = {"text": message}
    resp = requests.post(webhook_url, json=payload, timeout=10)
    resp.raise_for_status()


def send_email(server: str, port: int, username: str, password: str, recipient: str, subject: str, body: str) -> None:
    """Send an email with the given content."""
    import smtplib
    from email.message import EmailMessage

    msg = EmailMessage()
    msg["From"] = username
    msg["To"] = recipient
    msg["Subject"] = subject
    msg.set_content(body)

    with smtplib.SMTP(server, port) as smtp:
        smtp.starttls()
        smtp.login(username, password)
        smtp.send_message(msg)


def main():
    repo = os.environ.get("GITHUB_REPOSITORY")
    pr_number = os.environ.get("PR_NUMBER")
    token = os.environ.get("GITHUB_TOKEN")
    slack_webhook = os.environ.get("SLACK_WEBHOOK_URL")
    openai_key = os.environ.get("OPENAI_API_KEY")

    if not all([repo, pr_number, token, slack_webhook, openai_key]):
        raise SystemExit("Missing required environment variables")

    pr = fetch_pr_details(repo, pr_number, token)
    reviews = fetch_reviews(repo, pr_number, token)
    test_output = run_tests()
    test_summary = parse_test_summary(test_output)
    summary = generate_summary(openai_key, pr, reviews, test_summary)
    post_to_slack(slack_webhook, summary)

    if all(os.environ.get(v) for v in ["SMTP_SERVER", "SMTP_PORT", "SMTP_USERNAME", "SMTP_PASSWORD", "EMAIL_RECIPIENT"]):
        send_email(
            os.environ["SMTP_SERVER"],
            int(os.environ["SMTP_PORT"]),
            os.environ["SMTP_USERNAME"],
            os.environ["SMTP_PASSWORD"],
            os.environ["EMAIL_RECIPIENT"],
            f"Summary for PR #{pr_number}",
            summary,
        )


if __name__ == "__main__":
    main()
