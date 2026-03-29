# Conventional Commit Zh Skill Design

## Overview

Create a new Codex skill named `conventional-commit-zh` under `C:\Users\NoWat\.codex\skills`.

This skill is for requests to create and execute Git commits using Conventional Commits labels with Chinese summaries and Chinese commit bodies. The skill performs a semi-automatic commit flow:

1. Inspect the repository status and diffs
2. Generate a proposed commit message
3. Show the proposed message to the user for confirmation
4. After confirmation, run `git add -A` and `git commit`

## Goals

- Use Conventional Commits labels such as `feat`, `fix`, `refactor`, `docs`, `style`, `test`, `chore`, `perf`, `build`, `ci`, and `revert`
- Keep the commit title format as `<type>: <中文摘要>`
- Always include a Chinese body
- Require one user confirmation before executing the actual commit
- Allow automatic `git add -A` before commit
- Avoid dangerous Git actions such as `push`, `amend`, `rebase`, and `reset` unless explicitly requested

## Non-Goals

- Do not automatically push commits
- Do not rewrite Git history
- Do not automatically split commits, though the skill may recommend splitting mixed changes
- Do not enforce `scope` in every commit
- Do not create a fully automated no-confirmation commit workflow

## Triggering And Scope

The skill should trigger when the user asks Codex to:

- commit code
- generate a commit message and commit
- run `git commit`
- submit current changes with Conventional Commits style

The skill should be specific about the intended style:

- Conventional Commits type in English
- title summary in Chinese
- body in Chinese

The skill should not claim ownership of unrelated Git workflows such as push, merge, rebase, cherry-pick, or release tagging.

## Commit Message Rules

### Title

- Format: `<type>: <中文摘要>`
- Example: `feat: 新增书源批量导入功能`
- The title should be concise, direct, and free of trailing punctuation
- The title must not contain AI disclosure text or attribution

### Scope

- Scope is optional
- Use scope only when the modified area is clear and the added specificity improves readability
- If used, the format becomes `<type>(<scope>): <中文摘要>`

### Body

- A body is always required
- The body should be written in Chinese
- The body should summarize meaningful changes by module, feature area, or theme
- The body should not just repeat the title
- The body should avoid noisy implementation detail unless it materially helps understand the change

Recommended style:

```text
阅读页:
- 调整章节切换逻辑
- 修复边界条件下的闪退问题

书源管理:
- 新增批量导入入口
- 优化导入失败时的提示信息
```

## Execution Flow

The skill should follow this exact order:

1. Run `git status --short`
2. Inspect staged and unstaged diffs
3. Inspect recent commit history for local style context
4. Determine whether the changes appear cohesive enough for one commit
5. Generate a proposed full commit message
6. Show the proposed title and body to the user
7. Ask for one confirmation
8. After confirmation, run `git add -A`
9. Run `git commit` with the generated multi-line message
10. Report the result, current branch, and commit hash

## Safety Rules

- Never auto-push
- Never auto-amend
- Never auto-rebase
- Never auto-reset
- Never include AI attribution in the commit message
- Never proceed silently when sensitive files appear likely to be included

Sensitive or risky cases that should stop normal execution:

- `.env` files
- credentials files
- certificates or private keys
- obviously unrelated mixed changes
- empty working tree

When mixed changes are detected, the skill should stop and recommend splitting the commit instead of forcing a single message.

## Failure Handling

If there are no changes:

- Report that there is nothing to commit
- Do not run `git add` or `git commit`

If the user rejects the proposed message:

- Revise the message using the same repository state
- Show the updated proposal again
- Wait for confirmation before committing

If `git commit` fails:

- Report the concrete error
- Do not attempt dangerous recovery actions

If hooks fail:

- Surface the hook failure
- Do not bypass hooks automatically

## Skill Structure

Create a new skill directory:

- `C:\Users\NoWat\.codex\skills\conventional-commit-zh`

Minimal initial structure:

- `SKILL.md`
- `agents/openai.yaml`

No extra scripts are required in the first version. The workflow can be expressed directly in `SKILL.md`.

## Validation Plan

Validation should cover:

- YAML frontmatter correctness
- trigger description quality
- clear distinction from the existing `commit` skill
- correct requirement for one confirmation before commit
- correct requirement to always include a Chinese body
- correct ban on push, amend, rebase, and reset

Validation command:

```powershell
python C:\Users\NoWat\.codex\skills\.system\skill-creator\scripts\quick_validate.py C:\Users\NoWat\.codex\skills\conventional-commit-zh
```

## Open Decisions Resolved

- Use a new skill instead of modifying the existing `commit` skill
- Use Conventional Commits labels in English
- Use Chinese for the title summary
- Use Chinese for the body
- Always include a body
- Allow automatic `git add -A`
- Require one confirmation before the final commit command
