package com.winston.relati.backend.infrastructure.persistence.profile;

import com.winston.relati.backend.domain.model.profile.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
