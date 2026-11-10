---
name: feature-com-spec
description: "Use for Spec-Driven Development in Linketinder: defining a feature specification, breaking it into a plan, and implementing it iteratively with anonymity and changelog checks."
---

# Spec-Driven Development — Linketinder

Use this workflow for new features and substantial changes. Work in order:
**Especificação -> Plano -> Implementação iterativa**. Do not start implementation
before the specification and plan have been reviewed and accepted by the user.

## 1. Especificação

1. Explore the relevant code, tests, and `AGENTS.md` to understand existing
   behavior and architecture. Identify assumptions and unresolved product
   decisions before specifying the change.
2. Create the feature directory and save the spec at
   `docs/specs/<nome-da-feature>/spec.md`. Use a concise, lowercase kebab-case
   feature name for `<nome-da-feature>`.
3. Write observable requirements and acceptance criteria, scope and
   non-goals, user flows, relevant domain rules, API/data changes, and failure
   cases. Mark any decisions that still need user input rather than inventing
   product behavior.
4. Include a validation checklist in the spec. It MUST explicitly verify the
   Princípio do Anonimato for every affected read path and response:
   - Before a match, only competencies, education (if present), and
     description may be visible to the other party.
   - Personal or identifying information (including names, email, CPF, CNPJ,
     contact details, and address) is revealed only after a confirmed mutual
     match, and only to the parties in that match.
   - Enforce the rule in backend authorization/data selection and API
     serialization, not only in the UI. Include relevant list, detail, search,
     like, affinity, and match flows in the checklist as applicable.
5. Present the spec for user approval. Incorporate requested changes and do not
   move to planning until the user approves it.

## 2. Plano

1. After approval, create or update a plan in the feature's `spec.md` (or a
   clearly linked `plan.md` in the same feature directory if the plan is too
   large).
2. Break work into small, ordered, independently verifiable tasks. Identify
   dependencies, affected backend/frontend/database surfaces, and tests.
3. Give every task its own validation checklist. Every task checklist MUST
   include an explicit check of the Princípio do Anonimato, even if the task
   seems unrelated. For unrelated tasks, verify that existing anonymous
   behavior and data exposure remain unchanged.
4. Include the spec-level acceptance checklist in the plan and ensure its
   anonymity checks remain present. Present the plan for approval before
   implementation.

## 3. Implementação iterativa

1. Implement one planned task at a time, following existing project patterns
   and keeping the change scoped to the approved spec.
2. After each task, run the smallest relevant tests/build checks and complete
   that task's validation checklist, including its anonymity check. Add or
   update tests for anonymous pre-match responses and match-authorized
   disclosure whenever the task affects those paths.
3. Update `CHANGELOG.md` for every finalized task. This update is mandatory
   before committing: record the task's user-visible or architectural change
   under the appropriate version/Unreleased section. If the file does not yet
   exist, create it using a clear Unreleased section before the first task
   commit. Do not commit a finalized task while its changelog entry is missing.
4. Once all tasks are complete, run the full validation required by the spec,
   complete the spec-level acceptance checklist (including the anonymity
   review across all affected flows), and report any uncompleted item or
   limitation explicitly.
5. Request user approval at the agreed review gates. Do not commit unless
   explicitly requested or otherwise authorized by the user.

## Linketinder guardrails

- Treat the anonymity rule in the root `AGENTS.md` as a domain invariant for
  all feature work. Never rely on frontend-only masking to protect identities.
- A one-sided like is not a match and does not authorize identity disclosure.
- A match is mutual interest for the same candidate and vacancy; company
  ownership of the vacancy must also be respected.
- Do not assume education is currently modeled. If a feature adds it, keep it
  anonymous before match just like competencies and description.
- Review SQL scripts and database changes carefully; seed scripts may contain
  destructive operations. Never run destructive scripts against production.
