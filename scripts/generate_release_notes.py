#!/usr/bin/env python3
"""Generate release notes from merged PRs."""
import subprocess
import re
from collections import defaultdict

CATEGORY_PATTERNS = {
    'Added': re.compile(r'\badd(ed|s)?\b', re.IGNORECASE),
    'Fixed': re.compile(r'\bfix(ed|es)?\b|\bbug\b', re.IGNORECASE),
    'Changed': re.compile(r'\b(change|update|refactor)(d|s)?\b', re.IGNORECASE),
    'Removed': re.compile(r'\bremove(d|s)?\b', re.IGNORECASE),
}

PR_PATTERN = re.compile(r'\(#(\d+)\)')


def categorize(summary: str) -> str:
    for category, pattern in CATEGORY_PATTERNS.items():
        if pattern.search(summary):
            return category
    return 'Other'


def gather_pr_commits() -> list:
    output = subprocess.check_output(['git', 'log', '--pretty=%s'], text=True)
    entries = []
    for line in output.splitlines():
        line = line.strip()
        if not line:
            continue
        match = PR_PATTERN.search(line)
        if not match:
            continue
        pr_number = match.group(1)
        summary = PR_PATTERN.sub('', line).strip()
        entries.append((pr_number, summary))
    # deduplicate by PR number keeping first occurrence
    seen = set()
    unique_entries = []
    for pr, summary in entries:
        if pr not in seen:
            seen.add(pr)
            unique_entries.append((pr, summary))
    return unique_entries


def main():
    prs = gather_pr_commits()
    categorized = defaultdict(list)
    for pr_number, summary in prs:
        cat = categorize(summary)
        categorized[cat].append((pr_number, summary))

    with open('RELEASE_NOTES.md', 'w') as notes:
        notes.write('# Release Notes\n\n')
        for cat in ['Added', 'Changed', 'Fixed', 'Removed', 'Other']:
            entries = categorized.get(cat)
            if not entries:
                continue
            notes.write(f'## {cat}\n')
            for pr_number, summary in entries:
                notes.write(f'- PR #{pr_number}: {summary}\n')
            notes.write('\n')


if __name__ == '__main__':
    main()
