package com.winston.relati.backend.api.profile;

import com.winston.relati.backend.application.profile.CreateProfileService;
import com.winston.relati.backend.application.profile.CreateProfileService.AddressInput;
import com.winston.relati.backend.application.profile.CreateProfileService.AddressRole;
import com.winston.relati.backend.application.profile.CreateProfileService.CreateProfileCommand;
import java.time.OffsetDateTime;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final CreateProfileService createProfileService;

    @PostMapping
    public ResponseEntity<CreateProfileResponse> createProfile(
            @RequestBody CreateProfileRequest request) {
        CreateProfileCommand command = toCommand(request);
        Long id = createProfileService.createProfile(command);
        CreateProfileResponse response = new CreateProfileResponse();
        response.setProfileId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private CreateProfileCommand toCommand(CreateProfileRequest request) {
        CreateProfileCommand command = new CreateProfileCommand();
        command.setBirthName(request.getBirthName());
        command.setNickname(request.getNickname());
        command.setBirthDate(request.getBirthDate());
        command.setGender(request.getGender());
        command.setNationality(request.getNationality());
        command.setMaritalStatus(request.getMaritalStatus());
        command.setPhoneNumber(request.getPhoneNumber());
        command.setEmail(request.getEmail());

        if (request.getAddresses() != null) {
            Map<AddressRole, AddressInput> map = new EnumMap<>(AddressRole.class);
            putAddress(map, AddressRole.PERMANENT_ADDRESS, request.getAddresses().getPermanentAddress());
            putAddress(map, AddressRole.TEMPORARY_ADDRESS, request.getAddresses().getTemporaryAddress());
            putAddress(map, AddressRole.CURRENT_ADDRESS, request.getAddresses().getCurrentAddress());
            putAddress(map, AddressRole.CONTACT_ADDRESS, request.getAddresses().getContactAddress());
            putAddress(map, AddressRole.PLACE_OF_BIRTH, request.getAddresses().getPlaceOfBirth());
            putAddress(map, AddressRole.HOMETOWN, request.getAddresses().getHometown());
            putAddress(map, AddressRole.BIRTH_REGISTRATION_PLACE, request.getAddresses().getBirthRegistrationPlace());
            command.setAddresses(map);
        }

        return command;
    }

    private void putAddress(
            Map<AddressRole, AddressInput> target, AddressRole role, AddressDto src) {
        if (src == null) {
            return;
        }
        AddressInput input = new AddressInput();
        input.setAddressDetail(src.getAddressDetail());
        input.setProvinceCode(src.getProvinceCode());
        input.setWardCode(src.getWardCode());
        input.setCountry(src.getCountry());
        target.put(role, input);
    }

    @ExceptionHandler(CreateProfileService.BirthNameRequiredException.class)
    public ResponseEntity<ErrorResponse> handleBirthNameRequired() {
        return error("BIRTH_NAME_REQUIRED", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreateProfileService.ProvinceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProvinceNotFound() {
        return error("PROVINCE_NOT_FOUND", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreateProfileService.WardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWardNotFound() {
        return error("WARD_NOT_FOUND", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreateProfileService.IncompleteAddressException.class)
    public ResponseEntity<ErrorResponse> handleIncompleteAddress() {
        return error("INVALID_WARD_PROVINCE_MAPPING", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> error(String code, HttpStatus status) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(code);
        return ResponseEntity.status(status).body(response);
    }

    public static class CreateProfileRequest {
        private String birthName;
        private String nickname;
        private OffsetDateTime birthDate;
        private String gender;
        private String nationality;
        private String maritalStatus;
        private String phoneNumber;
        private String email;
        private AddressesDto addresses;

        public String getBirthName() {
            return birthName;
        }

        public void setBirthName(String birthName) {
            this.birthName = birthName;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public OffsetDateTime getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(OffsetDateTime birthDate) {
            this.birthDate = birthDate;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public AddressesDto getAddresses() {
            return addresses;
        }

        public void setAddresses(AddressesDto addresses) {
            this.addresses = addresses;
        }
    }

    public static class AddressesDto {
        private AddressDto permanentAddress;
        private AddressDto temporaryAddress;
        private AddressDto currentAddress;
        private AddressDto contactAddress;
        private AddressDto placeOfBirth;
        private AddressDto hometown;
        private AddressDto birthRegistrationPlace;

        public AddressDto getPermanentAddress() {
            return permanentAddress;
        }

        public void setPermanentAddress(AddressDto permanentAddress) {
            this.permanentAddress = permanentAddress;
        }

        public AddressDto getTemporaryAddress() {
            return temporaryAddress;
        }

        public void setTemporaryAddress(AddressDto temporaryAddress) {
            this.temporaryAddress = temporaryAddress;
        }

        public AddressDto getCurrentAddress() {
            return currentAddress;
        }

        public void setCurrentAddress(AddressDto currentAddress) {
            this.currentAddress = currentAddress;
        }

        public AddressDto getContactAddress() {
            return contactAddress;
        }

        public void setContactAddress(AddressDto contactAddress) {
            this.contactAddress = contactAddress;
        }

        public AddressDto getPlaceOfBirth() {
            return placeOfBirth;
        }

        public void setPlaceOfBirth(AddressDto placeOfBirth) {
            this.placeOfBirth = placeOfBirth;
        }

        public AddressDto getHometown() {
            return hometown;
        }

        public void setHometown(AddressDto hometown) {
            this.hometown = hometown;
        }

        public AddressDto getBirthRegistrationPlace() {
            return birthRegistrationPlace;
        }

        public void setBirthRegistrationPlace(AddressDto birthRegistrationPlace) {
            this.birthRegistrationPlace = birthRegistrationPlace;
        }
    }

    public static class AddressDto {
        private String addressDetail;
        private String provinceCode;
        private String wardCode;
        private String country;

        public String getAddressDetail() {
            return addressDetail;
        }

        public void setAddressDetail(String addressDetail) {
            this.addressDetail = addressDetail;
        }

        public String getProvinceCode() {
            return provinceCode;
        }

        public void setProvinceCode(String provinceCode) {
            this.provinceCode = provinceCode;
        }

        public String getWardCode() {
            return wardCode;
        }

        public void setWardCode(String wardCode) {
            this.wardCode = wardCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    public static class CreateProfileResponse {
        private Long profileId;

        public Long getProfileId() {
            return profileId;
        }

        public void setProfileId(Long profileId) {
            this.profileId = profileId;
        }
    }

    public static class ErrorResponse {
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
