package com.winston.relati.backend.infrastructure.persistence.reference;

import com.winston.relati.backend.domain.model.reference.AdministrativeUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrativeUnitRepository
        extends JpaRepository<AdministrativeUnit, Integer> {}

