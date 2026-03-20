package com.winston.relati.backend.domain.reference;

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
    @Column(name = "ward_code")
    private String wardCode;

    @Column(name = "province_code")
    private String provinceCode;

    @Column(name = "ward_name")
    private String wardName;
}
