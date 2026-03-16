package com.winston.relati.backend.application.profile;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.winston.relati.backend.application.profile.CreateProfileService.AddressInput;
import com.winston.relati.backend.application.profile.CreateProfileService.AddressRole;
import com.winston.relati.backend.application.profile.CreateProfileService.BirthNameRequiredException;
import com.winston.relati.backend.application.profile.CreateProfileService.CreateProfileCommand;
import com.winston.relati.backend.application.profile.CreateProfileService.IncompleteAddressException;
import com.winston.relati.backend.application.profile.CreateProfileService.ProvinceNotFoundException;
import com.winston.relati.backend.application.profile.CreateProfileService.WardNotFoundException;
import com.winston.relati.backend.domain.profile.Profile;
import com.winston.relati.backend.infrastructure.persistence.profile.AddressRepository;
import com.winston.relati.backend.infrastructure.persistence.profile.LocationRepository;
import com.winston.relati.backend.infrastructure.persistence.profile.ProfileRepository;
import com.winston.relati.backend.infrastructure.persistence.reference.ProvinceRepository;
import com.winston.relati.backend.infrastructure.persistence.reference.WardRepository;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CreateProfileServiceTest {

    private ProfileRepository profileRepository;
    private LocationRepository locationRepository;
    private AddressRepository addressRepository;
    private ProvinceRepository provinceRepository;
    private WardRepository wardRepository;

    private CreateProfileService service;

    @BeforeEach
    void setUp() {
        profileRepository = Mockito.mock(ProfileRepository.class);
        locationRepository = Mockito.mock(LocationRepository.class);
        addressRepository = Mockito.mock(AddressRepository.class);
        provinceRepository = Mockito.mock(ProvinceRepository.class);
        wardRepository = Mockito.mock(WardRepository.class);

        service =
                new CreateProfileService(
                        profileRepository,
                        locationRepository,
                        addressRepository,
                        provinceRepository,
                        wardRepository);
    }

    @Test
    void createProfile_success_withAddresses() {
        CreateProfileCommand command = new CreateProfileCommand();
        command.setBirthName("John Doe");

        AddressInput input = new AddressInput();
        input.setProvinceCode("01");
        input.setWardCode("001");

        Map<AddressRole, AddressInput> addresses = new EnumMap<>(AddressRole.class);
        addresses.put(AddressRole.PERMANENT_ADDRESS, input);
        command.setAddresses(addresses);

        Profile savedProfile = new Profile();
        savedProfile.setIndividualId(1L);
        when(profileRepository.save(any(Profile.class))).thenReturn(savedProfile);
        when(provinceRepository.existsByProvinceCode("01")).thenReturn(true);
        when(wardRepository.existsByWardCodeAndProvinceCode("001", "01")).thenReturn(true);

        Long id = service.createProfile(command);

        assertThat(id).isEqualTo(1L);
    }

    @Test
    void createProfile_rejects_whenBirthNameMissing() {
        CreateProfileCommand command = new CreateProfileCommand();

        assertThatThrownBy(() -> service.createProfile(command))
                .isInstanceOf(BirthNameRequiredException.class);
    }

    @Test
    void createProfile_rejects_whenProvinceNotFound() {
        CreateProfileCommand command = new CreateProfileCommand();
        command.setBirthName("John Doe");

        AddressInput input = new AddressInput();
        input.setProvinceCode("99");
        input.setWardCode("001");

        Map<AddressRole, AddressInput> addresses = new EnumMap<>(AddressRole.class);
        addresses.put(AddressRole.PERMANENT_ADDRESS, input);
        command.setAddresses(addresses);

        Profile savedProfile = new Profile();
        savedProfile.setIndividualId(1L);
        when(profileRepository.save(any(Profile.class))).thenReturn(savedProfile);
        when(provinceRepository.existsByProvinceCode("99")).thenReturn(false);

        assertThatThrownBy(() -> service.createProfile(command))
                .isInstanceOf(ProvinceNotFoundException.class);
    }

    @Test
    void createProfile_rejects_whenWardNotFound() {
        CreateProfileCommand command = new CreateProfileCommand();
        command.setBirthName("John Doe");

        AddressInput input = new AddressInput();
        input.setProvinceCode("01");
        input.setWardCode("999");

        Map<AddressRole, AddressInput> addresses = new EnumMap<>(AddressRole.class);
        addresses.put(AddressRole.PERMANENT_ADDRESS, input);
        command.setAddresses(addresses);

        Profile savedProfile = new Profile();
        savedProfile.setIndividualId(1L);
        when(profileRepository.save(any(Profile.class))).thenReturn(savedProfile);
        when(provinceRepository.existsByProvinceCode("01")).thenReturn(true);
        when(wardRepository.existsByWardCodeAndProvinceCode("999", "01")).thenReturn(false);

        assertThatThrownBy(() -> service.createProfile(command))
                .isInstanceOf(WardNotFoundException.class);
    }

    @Test
    void createProfile_rejects_whenAddressIncomplete() {
        CreateProfileCommand command = new CreateProfileCommand();
        command.setBirthName("John Doe");

        AddressInput input = new AddressInput();
        input.setProvinceCode("01");

        Map<AddressRole, AddressInput> addresses = new EnumMap<>(AddressRole.class);
        addresses.put(AddressRole.PERMANENT_ADDRESS, input);
        command.setAddresses(addresses);

        Profile savedProfile = new Profile();
        savedProfile.setIndividualId(1L);
        when(profileRepository.save(any(Profile.class))).thenReturn(savedProfile);

        assertThatThrownBy(() -> service.createProfile(command))
                .isInstanceOf(IncompleteAddressException.class);
    }
}
