# PROJECT_CHARTER

## 1. Mục tiêu dự án

Relationship Management System (RMS) là một phần mềm giúp một cá nhân quản lý, quan sát và phân tích các mối quan hệ trong đời sống của chính mình. Khác với CRM hay BRM trong kinh doanh, hệ thống này tập trung vào đời sống cá nhân, nhằm giúp người dùng hiểu rõ hơn các mối quan hệ xung quanh và những biến động xảy ra theo thời gian.

Mục tiêu cốt lõi của dự án là:

* Tạo ra một hệ thống lưu trữ có cấu trúc về con người và mối quan hệ.
* Hỗ trợ phân tích và trực quan hóa mạng lưới quan hệ cá nhân.
* Đặt nền móng cho các chức năng phân tích nâng cao bằng AI trong các phiên bản sau.

## 2. Phạm vi dự án

### Version 1 (MVP)

* Quản lý hồ sơ cá nhân (Profile Management).
* Quản lý mối quan hệ giữa các hồ sơ (Relationship Management).
* Trực quan hóa mạng lưới quan hệ (Network Visualization).

### Version 2

* Geological / Geographical mapping (gắn mối quan hệ với không gian địa lý).

### Version 3

* AI crawl và phân tích dữ liệu từ mạng xã hội (Facebook, Instagram, Google, …).
* Tự động fetch và đồng bộ dữ liệu vào profile.

### Version 4

* AI phân tích âm thanh (ví dụ: giọng nói, cuộc gọi, ghi âm).

## 3. Non-goals (Ngoài phạm vi)

* Không xây dựng hệ thống CRM cho doanh nghiệp.
* Không phục vụ mục đích giám sát hoặc theo dõi trái phép.
* Không triển khai các chức năng AI nâng cao trong Version 1.
* Không tích hợp mạng xã hội hoặc phân tích dữ liệu bên thứ ba ở giai đoạn MVP.

## 4. Đối tượng sử dụng

* Cá nhân muốn quản lý và phân tích các mối quan hệ trong cuộc sống.
* Người dùng kỹ thuật hoặc bán kỹ thuật, chấp nhận hệ thống ở mức công cụ cá nhân.

## 5. Yêu cầu chức năng (Functional Requirements)

### 5.1 Profile Management

#### Create Profile

Thông tin một profile bao gồm:

* Họ, chữ đệm và tên khai sinh (Tên là mandatory, các trường còn lại optional).
* Tên gọi khác / biệt danh.
* Ngày, tháng, năm sinh.
* Ngày, tháng, năm chết hoặc mất tích.
* Giới tính.
* Nơi sinh.
* Nơi đăng ký khai sinh.
* Nơi thường trú.
* Nơi tạm trú.
* Nơi ở hiện tại.
* Quê quán.
* Địa chỉ liên hệ.

Các chức năng cơ bản:

* Create profile.
* Read profile.
* Update profile.
* Delete profile.

### 5.2 Relationship Management

#### Create Relationship

Quy trình tạo mối quan hệ:

* B1: Chọn profile đã tồn tại.
* B2: Chọn loại mối quan hệ.
* B3: Click Submit.
* B4: Hệ thống kiểm tra trùng loại mối quan hệ trong trường hợp quan hệ chỉ cho phép một (ví dụ: cha, mẹ, vợ/chồng).

Các chức năng cơ bản:

* Create relationship.
* Read relationship.
* Update relationship.
* Delete relationship.

## 6. Nguyên tắc thiết kế

* Dữ liệu là trung tâm (data-first).
* Mô hình hóa rõ ràng giữa con người và mối quan hệ.
* Thiết kế mở rộng được cho các version sau.
* Tránh overengineering trong Version 1.

## 7. Công nghệ và ràng buộc

* Version 1 ưu tiên sự đơn giản, dễ maintain.
* Không phụ thuộc vào dịch vụ bên thứ ba.
* Kiến trúc và data model phải sẵn sàng cho mở rộng AI trong tương lai.

## 8. Definition of Done

Một chức năng được xem là hoàn thành khi:

* Đáp ứng đúng requirement trong phạm vi version hiện tại.
* CRUD hoạt động ổn định.
* Dữ liệu lưu trữ nhất quán.
* Không phá vỡ các chức năng hiện có.
* Có thể sử dụng để trực quan hóa mạng lưới quan hệ ở mức cơ bản.
