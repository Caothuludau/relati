package com.winston.relati.backend.domain.model.reference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "def_administrative_regions")
@Getter
@Setter
@NoArgsConstructor
public class AdministrativeRegion {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "code_name")
    private String codeName;

    @Column(name = "code_name_en")
    private String codeNameEn;
}

