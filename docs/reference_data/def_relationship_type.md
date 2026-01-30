# DEF_RELATIONSHIP_TYPE

## 1. Purpose

This file defines all valid relationship types used in RMS Version 1.

- This is reference data.
- NOT managed via normal CRUD.
- Used by Application Layer to enforce:
    - isOnly constraint
    - directionality
    - reverse relationship creation

Source of truth: Database Design – def_relationship_type table.

---

## 2. Schema Reference

Table: def_relationship_type

| Column               | Type      | Description |
|----------------------|-----------|-------------|
| rela_type_id         | BIGINT PK | Unique identifier |
| rela_type_name_us    | VARCHAR   | English name |
| rela_type_name_vn    | VARCHAR   | Vietnamese name |
| definition           | TEXT      | Meaning of the relationship |
| note                 | TEXT      | Clarification / scope |
| is_bidirectional     | BOOLEAN   | true = single record, false = directional |
| reverse_type_id      | BIGINT FK | Self-reference, nullable |
| is_only              | BOOLEAN   | Only one ACTIVE relationship allowed |

---

## 3. Core Rules

- If is_bidirectional = true  
  → create ONE relationship record.
- If is_bidirectional = false and reverse_type_id != null  
  → create TWO records (main + reverse).
- If is_only = true  
  → a profile can have at most ONE ACTIVE relationship of this type.

---

## 4. Relationship Type Definitions

### 4.1 Biological Parent – Child

| English | Vietnamese | is_bidirectional | reverse | is_only | Note |
|-------|------------|------------------|---------|---------|------|
| Father (Biological) | Cha ruột | false | Son / Daughter | true | Blood relation only |
| Mother (Biological) | Mẹ ruột | false | Son / Daughter | true | Blood relation only |
| Son (Biological) | Con trai ruột | false | Father / Mother | false | |
| Daughter (Biological) | Con gái ruột | false | Father / Mother | false | |

---

### 4.2 Grandparents / Grandchildren

Examples:

| English | Vietnamese | Direction |
|-------|------------|-----------|
| Paternal Grandfather | Ông nội | directional |
| Maternal Grandmother | Bà ngoại | directional |
| Grandson (Paternal) | Cháu nội (trai) | directional |
| Granddaughter (Maternal) | Cháu ngoại (gái) | directional |

Notes:
- Lineage (paternal / maternal) MUST be preserved.
- Reverse types are explicit.

---

### 4.3 Siblings

| English | Vietnamese | is_only |
|-------|------------|---------|
| Older Brother | Anh trai | false |
| Younger Brother | Em trai | false |
| Older Sister | Chị gái | false |
| Younger Sister | Em gái | false |

Constraints:
- Same biological parents.
- Directional by age.

---

### 4.4 Marriage & In-law

| English | Vietnamese | is_bidirectional | is_only |
|-------|------------|------------------|---------|
| Husband | Chồng | true | true |
| Wife | Vợ | true | true |

In-law relationships are directional and NOT biological.

---

### 4.5 Adoption & Legal

| English | Vietnamese | Type |
|-------|------------|------|
| Adoptive Father | Cha nuôi | legal |
| Adopted Son | Con trai nuôi | legal |
| Legal Guardian | Người giám hộ | legal |
| Ward | Người được giám hộ | one-way |

---

### 4.6 Social & Emotional

| English | Vietnamese | is_only | Note |
|-------|------------|---------|------|
| Friend | Bạn bè | false | Bidirectional |
| Close Friend | Bạn thân | false | |
| Best Friend | Bạn thân nhất | true | Limit to 1 |
| Lover | Người yêu | false | Pre-marriage |

---

### 4.7 Work & Mentorship

| English | Vietnamese | Direction |
|-------|------------|-----------|
| Manager | Sếp | Manager → Subordinate |
| Subordinate | Cấp dưới | reverse |
| Mentor | Người cố vấn | directional |
| Mentee | Người được cố vấn | directional |

---

## 5. Notes for Application Layer

- Never infer reverse relationships.
- Never hardcode relationship names.
- Always rely on relationshipTypeId + flags.
