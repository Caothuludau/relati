package com.winston.relati.backend.domain.model.reference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "def_wards")
@Getter
@Setter
@NoArgsConstructor
public class Ward {

    @Id
    @Column(name = "code")
    private String wardCode;

    @Column(name = "province_code")
    private String provinceCode;

    @Column(name = "name")
    private String name;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "full_name_en")
    private String fullNameEn;

    @Column(name = "code_name")
    private String codeName;

    @Column(name = "administrative_unit_id")
    private Integer administrativeUnitId;
}
