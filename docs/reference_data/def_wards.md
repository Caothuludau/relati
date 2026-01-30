# DEF_WARDS

## Purpose

Reference list of wards / districts.

- Child of def_provinces.
- Used to validate administrative hierarchy.

---

## Schema

Table: def_wards

| Column | Description |
|------|-------------|
| code | Ward/District code (PK) |
| province_code | Parent province (FK) |
| name | Vietnamese name |
| name_en | English name |
| full_name | Full Vietnamese name |
| full_name_en | Full English name |
| code_name | English code |
| administrative_unit_id | FK |

---

## Rules

- ward_code MUST belong to province_code.
- Validation happens in Application Layer.
- Data is immutable in Version 1.
