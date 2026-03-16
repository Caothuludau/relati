package com.winston.relati.backend.infrastructure.persistence.profile;

import com.winston.relati.backend.domain.profile.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
