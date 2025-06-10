import os
import openai
import subprocess
import requests

# Setup
openai.api_key = os.getenv("OPENAI_API_KEY")
GITHUB_TOKEN = os.getenv("GIT_TOKEN")
REPO = os.getenv("GITHUB_REPOSITORY")
PR_NUMBER = os.getenv("PR_NUMBER")

# Step 1: Get changed files from the PR branch
def get_changed_files():
    result = subprocess.run(["git", "diff", "--name-only", "origin/main...HEAD"], capture_output=True, text=True)
    return result.stdout.strip().splitlines()

# Step 2: Read file content safely
def read_file_content(filepath, max_bytes=2000):
    try:
        with open(filepath, "r", encoding="utf-8") as f:
            content = f.read()
            return content[:max_bytes]  # Truncate long files
    except Exception as e:
        return f"[Could not read file {filepath}: {e}]"

# Step 3: Build the prompt
def build_prompt(file_data):
    prompt = """You are an AI reviewer for a Spring Boot project.

		Analyze the following Java and configuration files for:
		- Presence of documentation: JavaDoc comments, meaningful inline comments, updated README files.
		- Test coverage: Are corresponding test classes or methods present for newly added or changed code?
		- Spring-specific structure: Use of annotations, controller/service/repo layers, and configuration consistency.

		Give a concise review with suggestions if documentation or testing is missing.
		"""


    for file, content in file_data.items():
        prompt += f"\n---\nFilename: {file}\n\n{content}\n"

    return prompt

# Step 4: Call OpenAI API
def ask_openai(prompt):
    response = openai.ChatCompletion.create(
        model="gpt-4",
        messages=[{"role": "user", "content": prompt}],
        temperature=0.3,
        max_tokens=1000
    )
    return response["choices"][0]["message"]["content"]

# Step 5: Optional - Post comment to PR
def post_comment_to_pr(comment):
    if not GITHUB_TOKEN or not PR_NUMBER:
        print("GITHUB_TOKEN or PR_NUMBER not set. Skipping PR comment.")
        return

    url = f"https://api.github.com/repos/{REPO}/issues/{PR_NUMBER}/comments"
    headers = {
        "Authorization": f"Bearer {GITHUB_TOKEN}",
        "Accept": "application/vnd.github.v3+json"
    }
    data = {"body": comment}
    response = requests.post(url, headers=headers, json=data)
    print("Posted review comment to PR:", response.status_code)

# Main Execution
if __name__ == "__main__":
    print("🔍 Fetching changed files...")
    files = get_changed_files()
    print("🧠 Reading file contents...")

	# Java and Spring Boot relevant files
	file_data = {f: read_file_content(f) for f in files if f.endswith(('.java', '.md', '.yml', '.yaml', '.properties', 'README', 'README.md'))}

    print("📦 Building prompt...")
    prompt = build_prompt(file_data)

    print("🤖 Calling OpenAI API...")
    ai_feedback = ask_openai(prompt)
    print("📝 AI Review Summary:\n")
    print(ai_feedback)

    # Uncomment this to post comment automatically
    # post_comment_to_pr(ai_feedback)
