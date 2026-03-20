package com.winston.relati.backend.infrastructure.persistence.reference;

import com.winston.relati.backend.domain.model.reference.AdministrativeRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrativeRegionRepository
        extends JpaRepository<AdministrativeRegion, Integer> {}

