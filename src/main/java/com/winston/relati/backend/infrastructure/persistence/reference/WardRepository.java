package com.winston.relati.backend.infrastructure.persistence.reference;

import com.winston.relati.backend.domain.model.reference.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepository extends JpaRepository<Ward, String> {

    boolean existsByWardCodeAndProvinceCode(String wardCode, String provinceCode);
}
