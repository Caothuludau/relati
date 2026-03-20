package com.winston.relati.backend.infrastructure.persistence.relationship;

import com.winston.relati.backend.domain.model.relationship.RelationshipType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipTypeRepository extends JpaRepository<RelationshipType, Long> {
}
