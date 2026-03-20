package com.winston.relati.backend.application.profile;

import com.winston.relati.backend.domain.model.profile.Address;
import com.winston.relati.backend.domain.model.profile.Location;
import com.winston.relati.backend.domain.model.profile.Profile;
import com.winston.relati.backend.infrastructure.persistence.profile.AddressRepository;
import com.winston.relati.backend.infrastructure.persistence.profile.LocationRepository;
import com.winston.relati.backend.infrastructure.persistence.profile.ProfileRepository;
import com.winston.relati.backend.infrastructure.persistence.reference.ProvinceRepository;
import com.winston.relati.backend.infrastructure.persistence.reference.WardRepository;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CreateProfileService {

    private final ProfileRepository profileRepository;
    private final LocationRepository locationRepository;
    private final AddressRepository addressRepository;
    private final ProvinceRepository provinceRepository;
    private final WardRepository wardRepository;

    @Transactional
    public Long createProfile(CreateProfileCommand command) {
        if (!StringUtils.hasText(command.getBirthName())) {
            throw new BirthNameRequiredException();
        }

        OffsetDateTime now = OffsetDateTime.now();

        Profile profile = new Profile();
        profile.setBirthName(command.getBirthName());
        profile.setNickname(command.getNickname());
        profile.setBirthDate(command.getBirthDate());
        profile.setGender(command.getGender());
        profile.setNationality(command.getNationality());
        profile.setMaritalStatus(command.getMaritalStatus());
        profile.setPhoneNumber(command.getPhoneNumber());
        profile.setEmail(command.getEmail());
        profile.setStartedAt(now);
        profile.setCreatedAt(now);
        profile.setUpdatedAt(now);

        profile = profileRepository.save(profile);

        Map<AddressRole, Long> locationMap = new HashMap<>();

        if (command.getAddresses() != null) {
            for (Map.Entry<AddressRole, AddressInput> entry : command.getAddresses().entrySet()) {
                AddressRole role = entry.getKey();
                AddressInput addr = entry.getValue();

                if (addr == null) {
                    continue;
                }

                if (!StringUtils.hasText(addr.getProvinceCode())
                        || !StringUtils.hasText(addr.getWardCode())) {
                    throw new IncompleteAddressException(role);
                }

                if (!provinceRepository.existsByProvinceCode(addr.getProvinceCode())) {
                    throw new ProvinceNotFoundException(addr.getProvinceCode());
                }

                if (!wardRepository.existsByWardCodeAndProvinceCode(
                        addr.getWardCode(), addr.getProvinceCode())) {
                    throw new WardNotFoundException(
                            addr.getWardCode(), addr.getProvinceCode());
                }

                Location location = new Location();
                location.setAddressDetail(addr.getAddressDetail());
                location.setProvinceCode(addr.getProvinceCode());
                location.setWardCode(addr.getWardCode());
                location.setCountry(
                        StringUtils.hasText(addr.getCountry()) ? addr.getCountry() : "VN");
                location.setStartedAt(now);
                location.setCreatedAt(now);
                location.setUpdatedAt(now);

                location = locationRepository.save(location);
                locationMap.put(role, location.getLocationId());
            }
        }

        if (!locationMap.isEmpty()) {
            Address address = new Address();
            address.setIndividualId(profile.getIndividualId());
            address.setPermanentAddress(locationMap.get(AddressRole.PERMANENT_ADDRESS));
            address.setTemporaryAddress(locationMap.get(AddressRole.TEMPORARY_ADDRESS));
            address.setCurrentAddress(locationMap.get(AddressRole.CURRENT_ADDRESS));
            address.setContactAddress(locationMap.get(AddressRole.CONTACT_ADDRESS));
            address.setPlaceOfBirth(locationMap.get(AddressRole.PLACE_OF_BIRTH));
            address.setHometown(locationMap.get(AddressRole.HOMETOWN));
            address.setBirthRegistrationPlace(
                    locationMap.get(AddressRole.BIRTH_REGISTRATION_PLACE));
            address.setStartedAt(now);
            address.setCreatedAt(now);
            address.setUpdatedAt(now);

            addressRepository.save(address);
        }

        return profile.getIndividualId();
    }

    public enum AddressRole {
        PERMANENT_ADDRESS,
        TEMPORARY_ADDRESS,
        CURRENT_ADDRESS,
        CONTACT_ADDRESS,
        PLACE_OF_BIRTH,
        HOMETOWN,
        BIRTH_REGISTRATION_PLACE
    }

    public static class AddressInput {
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

    public static class CreateProfileCommand {
        private String birthName;
        private String nickname;
        private OffsetDateTime birthDate;
        private String gender;
        private String nationality;
        private String maritalStatus;
        private String phoneNumber;
        private String email;
        private Map<AddressRole, AddressInput> addresses;

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

        public Map<AddressRole, AddressInput> getAddresses() {
            return addresses;
        }

        public void setAddresses(Map<AddressRole, AddressInput> addresses) {
            this.addresses = addresses;
        }
    }

    public static class BirthNameRequiredException extends RuntimeException {}

    public static class ProvinceNotFoundException extends RuntimeException {
        public ProvinceNotFoundException(String provinceCode) {
            super("Province not found: " + provinceCode);
        }
    }

    public static class WardNotFoundException extends RuntimeException {
        public WardNotFoundException(String wardCode, String provinceCode) {
            super(
                    "Ward not found: "
                            + wardCode
                            + " for province "
                            + provinceCode);
        }
    }

    public static class IncompleteAddressException extends RuntimeException {
        public IncompleteAddressException(AddressRole role) {
            super("Address is incomplete for role: " + Objects.toString(role));
        }
    }
}
