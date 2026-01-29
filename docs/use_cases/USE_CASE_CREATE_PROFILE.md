# USE_CASE_CREATE_PROFILE

## 1. Mục tiêu use case

Use case này mô tả **luồng nghiệp vụ tạo Profile** trong RMS Version 1, bao gồm:

* Tạo hồ sơ cá nhân (usr_profile).
* Tạo địa điểm (usr_location) nếu có.
* Gắn địa chỉ vào Profile thông qua usr_address.

Tài liệu này là **chuẩn nghiệp vụ** cho Coding Agent và **phải được tuân thủ tuyệt đối**.

Áp dụng cùng lúc:

* PROJECT_CHARTER.md
* ARCHITECTURE_OVERVIEW.md
* Database Design (usr_profile, usr_location, usr_address, def_provinces, def_wards)

---

## 2. Actor

* Primary Actor: User (qua UI hoặc API)
* System Actor: Application Layer

---

## 3. Input

```text
birthName              : Họ, chữ đệm và tên khai sinh (mandatory)
nickname               : Tên gọi khác (optional)
birthDate              : Ngày sinh (optional)
gender                 : Giới tính (optional)
nationality            : Quốc tịch (optional)
maritalStatus          : Tình trạng hôn nhân (optional)
phoneNumber            : Số điện thoại (optional)
email                  : Email (optional)

addresses (optional):
  permanentAddress
  temporaryAddress
  currentAddress
  contactAddress
  placeOfBirth
  hometown
  birthRegistrationPlace

Each address:
  addressDetail         : Số nhà, đường (optional)
  provinceCode          : Mã tỉnh/thành (mandatory nếu address tồn tại)
  wardCode              : Mã quận/huyện (mandatory nếu address tồn tại)
  country               : Quốc gia (default = VN)
```

---

## 4. Preconditions

* birthName không được null hoặc rỗng.
* Nếu address được truyền vào:

  * provinceCode phải tồn tại trong def_provinces.
  * wardCode phải tồn tại trong def_wards và thuộc provinceCode tương ứng.

Nếu không thỏa → reject request.

---

## 5. Postconditions

Sau khi use case thành công:

* 1 bản ghi usr_profile được tạo.
* 0..n bản ghi usr_location được tạo.
* Tối đa 1 bản ghi usr_address đang hiệu lực.
* Transaction được commit atomically.

---

## 6. Business Rules

### BR-01: Profile Identity

* birthName là định danh con người ở mức nghiệp vụ.
* Không enforce unique ở DB level.

### BR-02: Address Optionality

* Profile có thể tồn tại mà không có địa chỉ.

### BR-03: Location Reuse

* Location **không được** tái sử dụng giữa các Profile trong Version 1.
* Mỗi address role tạo location riêng.

---

## 7. Main Flow (Pseudo-code)

```pseudo
function createProfile(command):

  begin transaction

  // Step 1: Validate input
  if isBlank(command.birthName):
      throw BirthNameRequired

  // Step 2: Create Profile
  profile = new Profile(
      birthName     = command.birthName,
      nickname      = command.nickname,
      birthDate     = command.birthDate,
      gender        = command.gender,
      nationality   = command.nationality,
      maritalStatus = command.maritalStatus,
      phoneNumber   = command.phoneNumber,
      email         = command.email,
      startedAt     = now
  )

  save(profile)

  // Step 3: Create Locations (if any)
  locationMap = empty map

  for each addressRole in command.addresses:
      addr = command.addresses[addressRole]

      if addr is null:
          continue

      // Validate administrative units
      if not existsProvince(addr.provinceCode):
          throw ProvinceNotFound

      if not existsWard(addr.wardCode, addr.provinceCode):
          throw WardNotFound

      location = new Location(
          addressDetail = addr.addressDetail,
          provinceCode  = addr.provinceCode,
          wardCode      = addr.wardCode,
          country       = addr.country or 'VN',
          startedAt     = now
      )

      save(location)
      locationMap[addressRole] = location.id

  // Step 4: Create Address record (if any location exists)
  if locationMap is not empty:

      address = new Address(
          individualId           = profile.id,
          permanentAddress       = locationMap['permanentAddress'],
          temporaryAddress       = locationMap['temporaryAddress'],
          currentAddress         = locationMap['currentAddress'],
          contactAddress         = locationMap['contactAddress'],
          placeOfBirth           = locationMap['placeOfBirth'],
          hometown               = locationMap['hometown'],
          birthRegistrationPlace = locationMap['birthRegistrationPlace'],
          startedAt              = now
      )

      save(address)

  commit transaction

  return Success(profile.id)
```

---

## 8. Alternative Flows

### AF-01: Partial Address Input

* Nếu address thiếu provinceCode hoặc wardCode → reject toàn bộ request.

### AF-02: Invalid Administrative Mapping

* wardCode không thuộc provinceCode → reject.

---

## 9. Error Codes (gợi ý)

* BIRTH_NAME_REQUIRED
* PROVINCE_NOT_FOUND
* WARD_NOT_FOUND
* INVALID_WARD_PROVINCE_MAPPING

---

## 10. Ghi chú cho Coding Agent

* Không tạo Address nếu không có Location.
* Không merge nhiều address role vào một Location.
* Không cache def_provinces / def_wards trong Version 1.
* Transaction là bắt buộc.

---

## 11. Phạm vi

Use case này chỉ áp dụng cho Create Profile – RMS Version 1.
