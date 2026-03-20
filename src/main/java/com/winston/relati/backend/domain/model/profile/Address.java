package com.winston.relati.backend.domain.model.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usr_address")
@Getter
@Setter
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "individual_id", nullable = false)
    private Long individualId;

    @Column(name = "permanent_address")
    private Long permanentAddress;

    @Column(name = "temporary_address")
    private Long temporaryAddress;

    @Column(name = "current_address")
    private Long currentAddress;

    @Column(name = "contact_address")
    private Long contactAddress;

    @Column(name = "place_of_birth")
    private Long placeOfBirth;

    @Column(name = "hometown")
    private Long hometown;

    @Column(name = "birth_registration_place")
    private Long birthRegistrationPlace;

    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;

    @Column(name = "ended_at")
    private OffsetDateTime endedAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
