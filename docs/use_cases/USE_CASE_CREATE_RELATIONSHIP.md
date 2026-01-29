# USE_CASE_CREATE_RELATIONSHIP

## 1. Mục tiêu use case

Use case này mô tả **toàn bộ luồng nghiệp vụ tạo Relationship** giữa hai Profile trong RMS Version 1.

Tài liệu này là **chuẩn nghiệp vụ** cho Coding Agent:

* Không phải code.
* Không được đơn giản hóa.
* Pseudo-code phải được follow sát khi implement.

Use case này **bắt buộc tuân theo**:

* PROJECT_CHARTER.md
* ARCHITECTURE_OVERVIEW.md
* REFERENCE_DATA_RELATIONSHIP_TYPE.md

---

## 2. Actor

* Primary Actor: User (qua UI hoặc API)
* System Actor: Application Layer

---

## 3. Input

```text
fromProfileId      : ID của profile khởi tạo mối quan hệ
toProfileId        : ID của profile được liên kết
relationshipTypeId : ID của Relationship Type
startedAt          : Thời điểm bắt đầu quan hệ (optional, default = now)
```

---

## 4. Preconditions

* fromProfileId tồn tại và đang ACTIVE.
* toProfileId tồn tại và đang ACTIVE.
* fromProfileId ≠ toProfileId.
* relationshipTypeId tồn tại trong def_relationship_type.

Nếu bất kỳ điều kiện nào không thỏa → reject request.

---

## 5. Postconditions

Sau khi use case thành công:

* Ít nhất 1 bản ghi usr_relationship được tạo.
* Cardinality và direction được đảm bảo đúng theo RelationshipType.
* Transaction được commit atomically.

---

## 6. Business Rules

### BR-01: Cardinality (is_only)

Nếu `relationshipType.isOnly = true`:

* fromProfileId **không được** có relationship ACTIVE cùng relationshipType.

### BR-02: Direction & Reverse

* Nếu `relationshipType.isBidirectional = true`:

  * Chỉ tạo **1 bản ghi**.
* Nếu `relationshipType.isBidirectional = false`:

  * Phải tạo bản ghi reverse nếu `reverseTypeId != null`.

### BR-03: Status

* Relationship mới tạo có status = ACTIVE.

---

## 7. Main Flow (Pseudo-code)

```pseudo
function createRelationship(command):

  begin transaction

  // Step 1: Load Profiles
  fromProfile = loadProfile(command.fromProfileId)
  toProfile   = loadProfile(command.toProfileId)

  if fromProfile == null or toProfile == null:
      throw ProfileNotFound

  if fromProfile.id == toProfile.id:
      throw InvalidRelationshipSelf

  // Step 2: Load RelationshipType
  relType = loadRelationshipType(command.relationshipTypeId)

  if relType == null:
      throw RelationshipTypeNotFound

  // Step 3: Validate isOnly constraint
  if relType.isOnly == true:
      existing = findActiveRelationship(
          profileId = fromProfile.id,
          relationshipTypeId = relType.id
      )
      if existing exists:
          throw RelationshipTypeOnlyConstraintViolated

  // Step 4: Create main relationship
  mainRelationship = new Relationship(
      fromProfileId = fromProfile.id,
      toProfileId   = toProfile.id,
      relationshipTypeId = relType.id,
      status = ACTIVE,
      startedAt = command.startedAt or now
  )

  save(mainRelationship)

  // Step 5: Handle reverse relationship
  if relType.isBidirectional == false and relType.reverseTypeId != null:

      reverseType = loadRelationshipType(relType.reverseTypeId)

      if reverseType == null:
          throw ReverseRelationshipTypeNotFound

      if reverseType.isOnly == true:
          reverseExisting = findActiveRelationship(
              profileId = toProfile.id,
              relationshipTypeId = reverseType.id
          )
          if reverseExisting exists:
              throw ReverseRelationshipOnlyConstraintViolated

      reverseRelationship = new Relationship(
          fromProfileId = toProfile.id,
          toProfileId   = fromProfile.id,
          relationshipTypeId = reverseType.id,
          status = ACTIVE,
          startedAt = command.startedAt or now
      )

      save(reverseRelationship)

  commit transaction

  return Success(mainRelationship.id)
```

---

## 8. Alternative Flows

### AF-01: Duplicate Relationship Attempt

* Nếu relationship cùng loại và cùng hướng đã tồn tại và ACTIVE → reject.

### AF-02: Reverse Conflict

* Nếu reverse relationship vi phạm isOnly → rollback toàn bộ transaction.

---

## 9. Error Codes (gợi ý)

* PROFILE_NOT_FOUND
* RELATIONSHIP_TYPE_NOT_FOUND
* INVALID_SELF_RELATIONSHIP
* RELATIONSHIP_TYPE_ONLY_VIOLATION
* REVERSE_RELATIONSHIP_ONLY_VIOLATION

---

## 10. Ghi chú cho Coding Agent

* Không tối ưu bằng cách bỏ transaction.
* Không đẩy logic này xuống DB trigger.
* Không tự suy diễn thêm rule ngoài tài liệu.
* Nếu không chắc, **fail fast**.

---

## 11. Phạm vi

Use case này chỉ áp dụng cho Create Relationship – RMS Version 1.
