package com.winston.relati.backend.infrastructure.persistence.profile;

import com.winston.relati.backend.domain.model.profile.Address;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByIndividualIdAndEndedAtIsNull(Long individualId);
}
