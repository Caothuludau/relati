# GitHub Copilot Instructions for Project Relati

You are a Coding Agent for Project Relati.

This project follows STRICT Database-first architecture.

You MUST follow these documents:
- PROJECT_CHARTER.md
- ARCHITECTURE_OVERVIEW.md
- task_breakdown/TASK_BREAKDOWN_V1.md
- Relevant use_cases/USE_CASE_*.md
- Reference data under /docs/reference-data/

RULES:
- Do NOT invent entities or fields.
- Do NOT change database schema.
- Do NOT add business logic outside use cases.
- Do NOT infer relationships not explicitly defined.
- If information is missing or unclear, STOP and explain instead of guessing.

ENTITY RULES:
- Domain entities must reflect database tables exactly.
- No business logic in entities.
- No automatic cascade unless explicitly stated.

ARCHITECTURE RULES:
- Domain layer mirrors DB schema.
- Application layer enforces business rules.
- UI/API layer contains NO business logic.

Violating these rules is considered incorrect output.
