package com.winston.relati.backend.infrastructure.persistence.reference;

import com.winston.relati.backend.domain.reference.Province;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<Province, String> {

    boolean existsByProvinceCode(String provinceCode);
}
