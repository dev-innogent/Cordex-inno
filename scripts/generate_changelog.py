#!/usr/bin/env python3
"""Generate CHANGELOG entries from git history."""
import subprocess
import re
from collections import defaultdict

CATEGORY_PATTERNS = {
    'Added': re.compile(r'\badd(ed|s)?\b', re.IGNORECASE),
    'Fixed': re.compile(r'\bfix(ed|es)?\b|\bbug\b', re.IGNORECASE),
    'Changed': re.compile(r'\b(change|update|refactor)(d|s)?\b', re.IGNORECASE),
    'Removed': re.compile(r'\bremove(d|s)?\b', re.IGNORECASE),
}


def categorize(message: str) -> str:
    for category, pattern in CATEGORY_PATTERNS.items():
        if pattern.search(message):
            return category
    return 'Other'


def gather_commit_messages() -> list:
    output = subprocess.check_output([
        'git', 'log', '--pretty=%s'
    ], text=True)
    return [line.strip() for line in output.splitlines() if line.strip()]


def main():
    commits = gather_commit_messages()
    categorized = defaultdict(list)
    for msg in commits:
        category = categorize(msg)
        categorized[category].append(msg)

    with open('CHANGELOG.md', 'w') as changelog:
        changelog.write('# Changelog\n\n')
        for cat in ['Added', 'Changed', 'Fixed', 'Removed', 'Other']:
            entries = categorized.get(cat)
            if not entries:
                continue
            changelog.write(f'## {cat}\n')
            for entry in entries:
                changelog.write(f'- {entry}\n')
            changelog.write('\n')


if __name__ == '__main__':
    main()
