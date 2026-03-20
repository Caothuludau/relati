package com.winston.relati.backend.domain.model.relationship;

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
@Table(name = "usr_relationship")
@Getter
@Setter
@NoArgsConstructor
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relationship_id")
    private Long relationshipId;

    @Column(name = "from_user_profile_id", nullable = false)
    private Long fromUserProfileId;

    @Column(name = "to_user_profile_id", nullable = false)
    private Long toUserProfileId;

    @Column(name = "relationship_type_id", nullable = false)
    private Long relationshipTypeId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;

    @Column(name = "ended_at")
    private OffsetDateTime endedAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
