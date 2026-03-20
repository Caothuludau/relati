package com.winston.relati.backend.infrastructure.persistence.reference;

import com.winston.relati.backend.domain.model.reference.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {

    boolean existsByProvinceCode(String provinceCode);
}
