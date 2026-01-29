# TASK_BREAKDOWN_V1

## 1. Mục tiêu tài liệu

Tài liệu này phân rã công việc (task) cho RMS Version 1, **map trực tiếp 1–1 với các Use Case đã được chốt**:

* USE_CASE_CREATE_PROFILE.md
* USE_CASE_CREATE_RELATIONSHIP.md

Mục tiêu:

* Làm đầu vào chuẩn cho Orchestrator Agent.
* Ngăn Coding Agent tự mở rộng scope.
* Mỗi task độc lập, có thể review và test riêng.

---

## 2. Nguyên tắc chung cho mọi task

Mỗi task **bắt buộc** phải tuân theo:

* PROJECT_CHARTER.md
* ARCHITECTURE_OVERVIEW.md
* Database Design
* Use Case tương ứng

Coding Agent:

* Không được thay đổi schema.
* Không được thêm logic ngoài use case.
* Nếu thiếu thông tin → fail và báo lại Orchestrator.

---

## 3. Task Group A – Create Profile

(Map 1–1 với USE_CASE_CREATE_PROFILE.md)

### TASK-A1: Define Domain Entities for Profile

**Mục tiêu**

* Khai báo entity ánh xạ cho các bảng:

  * usr_profile
  * usr_location
  * usr_address

**Input**

* Database Design
* ARCHITECTURE_OVERVIEW.md

**Output**

* Domain entities với mapping đầy đủ field.

**Acceptance Criteria**

* Không thiếu field.
* Naming khớp schema.
* Không embed business logic.

---

### TASK-A2: Repository Layer for Profile & Address

**Mục tiêu**

* Implement repository cho:

  * Profile
  * Location
  * Address

**Input**

* Domain entities (TASK-A1)

**Output**

* CRUD repository cơ bản.

**Acceptance Criteria**

* Transaction support.
* Không logic nghiệp vụ.

---

### TASK-A3: Application Service – Create Profile

**Mục tiêu**

* Implement use case Create Profile đúng pseudo-code.

**Input**

* USE_CASE_CREATE_PROFILE.md

**Output**

* Application service / use case handler.

**Acceptance Criteria**

* Validate birthName.
* Validate province / ward.
* Tạo Profile → Location → Address trong 1 transaction.

---

### TASK-A4: API Layer – Create Profile Endpoint

**Mục tiêu**

* Expose API cho Create Profile.

**Input**

* Application service (TASK-A3)

**Output**

* REST endpoint.

**Acceptance Criteria**

* Request/response rõ ràng.
* Không xử lý nghiệp vụ.

---

### TASK-A5: Tests – Create Profile

**Mục tiêu**

* Viết test cho Create Profile.

**Input**

* TASK-A3

**Output**

* Unit + integration tests.

**Acceptance Criteria**

* Test success case.
* Test invalid province/ward.
* Test no-address case.

---

## 4. Task Group B – Create Relationship

(Map 1–1 với USE_CASE_CREATE_RELATIONSHIP.md)

### TASK-B1: Define Domain Entity for Relationship

**Mục tiêu**

* Khai báo entity ánh xạ cho:

  * usr_relationship
  * def_relationship_type

**Input**

* Database Design

**Output**

* Domain entities.

**Acceptance Criteria**

* Mapping đúng field.
* Self-FK reverseType được thể hiện rõ.

---

### TASK-B2: Repository Layer – Relationship

**Mục tiêu**

* Implement repository cho Relationship.

**Input**

* Domain entities (TASK-B1)

**Output**

* Query hỗ trợ tìm relationship ACTIVE theo profile + type.

**Acceptance Criteria**

* Không hardcode status.
* Query rõ ràng.

---

### TASK-B3: Application Service – Create Relationship

**Mục tiêu**

* Implement use case Create Relationship đúng pseudo-code.

**Input**

* USE_CASE_CREATE_RELATIONSHIP.md
* REFERENCE_DATA_RELATIONSHIP_TYPE.md

**Output**

* Application service / handler.

**Acceptance Criteria**

* Enforce isOnly.
* Handle reverse relationship.
* Transaction atomic.

---

### TASK-B4: API Layer – Create Relationship Endpoint

**Mục tiêu**

* Expose API cho Create Relationship.

**Input**

* Application service (TASK-B3)

**Output**

* REST endpoint.

**Acceptance Criteria**

* Không xử lý business rule.
* Trả error code đúng.

---

### TASK-B5: Tests – Create Relationship

**Mục tiêu**

* Viết test cho Create Relationship.

**Input**

* TASK-B3

**Output**

* Unit + integration tests.

**Acceptance Criteria**

* Test isOnly violation.
* Test bidirectional vs reverse.
* Test rollback khi reverse fail.

---

## 5. Thứ tự thực thi đề xuất

1. TASK-A1 → A2 → A3 → A5
2. TASK-B1 → B2 → B3 → B5
3. TASK-A4, TASK-B4 (sau cùng)

---

## 6. Definition of Done (Version 1)

* Hai use case Create Profile và Create Relationship chạy end-to-end.
* Không vi phạm rule trong reference data.
* Code pass test.
* Không phát sinh thêm entity hoặc logic ngoài tài liệu.
