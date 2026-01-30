# DEF_PROVINCES

## Purpose

Reference list of Vietnamese provinces and centrally governed cities.

- Read-only.
- Used for validating Location input.
- Source: postgres_CreateTables_vn_units.sql

---

## Schema

Table: def_provinces

| Column | Description |
|------|-------------|
| code | Province code (PK) |
| name | Vietnamese name |
| name_en | English name |
| full_name | Full Vietnamese name |
| full_name_en | Full English name |
| code_name | English code name |
| administrative_unit_id | FK |

---

## Rules

- Province code MUST exist for any Location.
- No caching in Version 1.
- No modification via application.
