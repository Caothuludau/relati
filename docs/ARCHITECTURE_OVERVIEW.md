# ARCHITECTURE_OVERVIEW

## 1. Mục tiêu tài liệu

Tài liệu này mô tả kiến trúc tổng thể của Relationship Management System (RMS) Version 1 **dựa trên Database Design đã được chốt**, nhằm:

* Làm chuẩn tham chiếu duy nhất cho Coding Agent.
* Đảm bảo kiến trúc logic khớp 1–1 với mô hình dữ liệu thực tế.
* Ngăn việc tự ý suy diễn domain hoặc đơn giản hóa sai bản chất quan hệ.

Mọi implementation **phải bám theo Database Design**. Domain Model không được phép mâu thuẫn với schema.

## 2. Kiến trúc tổng thể

RMS Version 1 sử dụng **Layered Architecture**, nhưng với định hướng **Database-first**:

* Database Design là ground truth.
* Domain Layer phản chiếu đúng cấu trúc dữ liệu.
* Application Layer thực thi rule không thể biểu diễn thuần bằng constraint DB.

Các lớp:

* Presentation Layer (API / UI)
* Application Layer (Use Case, Validation)
* Domain Layer (Entity ánh xạ DB)
* Infrastructure Layer (Persistence, Mapping, Visualization)

## 3. Domain Model (Mapping từ Database)

### 3.1 Reference / Definition Tables

#### RelationshipType (def_relationship_type)

Dữ liệu chuẩn định nghĩa loại mối quan hệ.

Thuộc tính chính:

* relaTypeId
* relaTypeNameUs
* relaTypeNameVn
* definition
* note
* isBidirectional
* reverseTypeId (self-reference)
* isOnly (chỉ cho phép duy nhất một quan hệ đang ACTIVE)

Nguyên tắc:

* RelationshipType là **reference data**, không CRUD qua UI thông thường.
* Quan hệ cha–con, sếp–cấp dưới được biểu diễn bằng reverseTypeId.
* Application Layer phải xử lý logic reverse khi tạo relationship.

#### Administrative Units (def_provinces, def_wards)

Nguồn dữ liệu địa giới hành chính 2 cấp, dùng cho Location.

Nguyên tắc:

* Dữ liệu chỉ đọc (read-only).
* Không chỉnh sửa, không replicate logic hành chính vào Domain.

### 3.2 User Data Tables

#### Profile (usr_profile)

Đại diện cho một cá nhân trong hệ thống.

Thuộc tính chính:

* individualId
* birthName
* nickname
* birthDate
* gender
* nationality
* maritalStatus
* phoneNumber
* email
* startedAt
* endedAt
* createdAt
* updatedAt

Nguyên tắc:

* Profile là aggregate root.
* Không chứa trực tiếp thông tin địa chỉ chi tiết.

#### Location (usr_location)

Đại diện cho một địa điểm cụ thể.

Thuộc tính chính:

* locationId
* addressDetail
* provinceCode (FK → def_provinces)
* wardCode (FK → def_wards)
* country
* startedAt
* endedAt
* createdAt
* updatedAt

Nguyên tắc:

* Location tách riêng để tái sử dụng cho nhiều ngữ cảnh.

#### Address (usr_address)

Liên kết Profile với các Location theo vai trò.

Thuộc tính chính:

* addressId
* individualId
* permanentAddress
* temporaryAddress
* currentAddress
* contactAddress
* placeOfBirth
* hometown
* birthRegistrationPlace
* startedAt
* endedAt
* createdAt
* updatedAt

Nguyên tắc:

* Address là entity phụ thuộc Profile.
* Một Profile có tối đa một bản ghi Address đang hiệu lực.

#### Relationship (usr_relationship)

Đại diện cho mối quan hệ giữa hai Profile.

Thuộc tính chính:

* relationshipId
* fromUserProfileId
* toUserProfileId
* relationshipTypeId
* status (PENDING, ACTIVE, ENDED, BLOCKED)
* startedAt
* endedAt
* createdAt
* updatedAt

Nguyên tắc:

* Relationship là entity độc lập.
* Cardinality và uniqueness không dựa hoàn toàn vào DB constraint.
* Application Layer phải enforce isOnly và reverse relationship.

## 4. Application Layer Responsibilities

### Create Profile

* Tạo usr_profile.
* Tạo usr_location (nếu có).
* Tạo usr_address liên kết location.
* Đảm bảo transaction atomic.

### Create Relationship

* Validate tồn tại của hai Profile.
* Validate relationshipTypeId.
* Nếu isOnly = true, kiểm tra không có relationship ACTIVE trùng loại.
* Nếu isBidirectional = false, tự động tạo reverse relationship (nếu có reverseTypeId).
* Khởi tạo status và startedAt.

## 5. Data Flow (Create Relationship)

1. API nhận request.
2. Application Layer load RelationshipType.
3. Validate constraint (isOnly, direction).
4. Persist usr_relationship.
5. Nếu cần, persist reverse relationship.
6. Commit transaction.

## 6. Nguyên tắc bắt buộc cho Coding Agent

* Không suy diễn thêm entity ngoài schema.
* Không gộp Location vào Profile.
* Không xử lý reverse relationship ở UI.
* Mọi logic liên quan isOnly, reverseTypeId phải nằm ở Application Layer.
* Nếu DB và Domain mâu thuẫn, DB đúng.

## 7. Phạm vi

Tài liệu này chỉ áp dụng cho Version 1: Create Profile và Create Relationship.
Các version sau sẽ có architecture extension riêng.
