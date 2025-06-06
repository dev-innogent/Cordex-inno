# Cordex-inno

This repository contains experiments and documentation for Cordex's innovative solutions. More details will be added soon.

## Generating the changelog
Run `scripts/generate_changelog.py` to update `CHANGELOG.md` based on commit messages. Entries are grouped under Added, Changed, Fixed, Removed, and Other.

## Posting PR summaries
The `scripts/post_summary.py` script can post a short GPT-generated summary of a pull request, its test results and review state to Slack and optionally email. The script reads configuration from environment variables:

- `GITHUB_REPOSITORY` – owner/repo
- `PR_NUMBER` – pull request number
- `GITHUB_TOKEN` – token for GitHub API access
- `SLACK_WEBHOOK_URL` – Slack webhook to post the message
- `OPENAI_API_KEY` – API key used for text generation
- `SMTP_SERVER`, `SMTP_PORT`, `SMTP_USERNAME`, `SMTP_PASSWORD`, `EMAIL_RECIPIENT` – optional email settings

Running the script will fetch PR details, execute the Maven test suite, generate a concise summary and send it to the configured destinations.
## Drafting release notes
Use `scripts/generate_release_notes.py` to create `RELEASE_NOTES.md` from merged pull requests. The script groups PR summaries into the same categories as the changelog.
