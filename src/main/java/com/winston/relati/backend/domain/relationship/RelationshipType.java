package com.winston.relati.backend.domain.relationship;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "def_relationship_type")
@Getter
@Setter
@NoArgsConstructor
public class RelationshipType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rela_type_id")
    private Long relaTypeId;

    @Column(name = "rela_type_name_us", nullable = false)
    private String relaTypeNameUs;

    @Column(name = "rela_type_name_vn", nullable = false)
    private String relaTypeNameVn;

    @Column(name = "definition")
    private String definition;

    @Column(name = "note")
    private String note;

    @Column(name = "is_bidirectional", nullable = false)
    private boolean isBidirectional;

    @Column(name = "reverse_type_id")
    private Long reverseTypeId;

    @Column(name = "is_only", nullable = false)
    private boolean isOnly;
}
