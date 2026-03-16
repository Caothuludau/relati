package com.winston.relati.backend.infrastructure.persistence.reference;

import com.winston.relati.backend.domain.reference.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WardRepository extends JpaRepository<Ward, String> {

    boolean existsByWardCodeAndProvinceCode(String wardCode, String provinceCode);
}
