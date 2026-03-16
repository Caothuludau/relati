package com.winston.relati.backend.infrastructure.persistence.relationship;

import com.winston.relati.backend.domain.relationship.Relationship;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {

    Optional<Relationship> findFirstByFromUserProfileIdAndRelationshipTypeIdAndStatus(
            Long fromUserProfileId, Long relationshipTypeId, String status);
}
