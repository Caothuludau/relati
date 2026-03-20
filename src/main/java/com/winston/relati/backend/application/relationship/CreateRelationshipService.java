package com.winston.relati.backend.application.relationship;

import com.winston.relati.backend.domain.model.profile.Profile;
import com.winston.relati.backend.domain.model.relationship.Relationship;
import com.winston.relati.backend.domain.model.relationship.RelationshipType;
import com.winston.relati.backend.infrastructure.persistence.profile.ProfileRepository;
import com.winston.relati.backend.infrastructure.persistence.relationship.RelationshipRepository;
import com.winston.relati.backend.infrastructure.persistence.relationship.RelationshipTypeRepository;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRelationshipService {

    private static final String STATUS_ACTIVE = "ACTIVE";

    private final ProfileRepository profileRepository;
    private final RelationshipRepository relationshipRepository;
    private final RelationshipTypeRepository relationshipTypeRepository;

    @Transactional
    public Long createRelationship(CreateRelationshipCommand command) {
        Profile fromProfile =
                profileRepository
                        .findById(command.getFromProfileId())
                        .orElseThrow(ProfileNotFoundException::new);
        Profile toProfile =
                profileRepository
                        .findById(command.getToProfileId())
                        .orElseThrow(ProfileNotFoundException::new);

        if (fromProfile.getIndividualId().equals(toProfile.getIndividualId())) {
            throw new InvalidSelfRelationshipException();
        }

        RelationshipType relType =
                relationshipTypeRepository
                        .findById(command.getRelationshipTypeId())
                        .orElseThrow(RelationshipTypeNotFoundException::new);

        if (relType.isOnly()) {
            Optional<Relationship> existing =
                    relationshipRepository
                            .findFirstByFromUserProfileIdAndRelationshipTypeIdAndStatus(
                                    fromProfile.getIndividualId(),
                                    relType.getRelaTypeId(),
                                    STATUS_ACTIVE);
            if (existing.isPresent()) {
                throw new RelationshipTypeOnlyConstraintViolatedException();
            }
        }

        OffsetDateTime startedAt =
                command.getStartedAt() != null ? command.getStartedAt() : OffsetDateTime.now();

        Relationship mainRelationship = new Relationship();
        mainRelationship.setFromUserProfileId(fromProfile.getIndividualId());
        mainRelationship.setToUserProfileId(toProfile.getIndividualId());
        mainRelationship.setRelationshipTypeId(relType.getRelaTypeId());
        mainRelationship.setStatus(STATUS_ACTIVE);
        mainRelationship.setStartedAt(startedAt);
        mainRelationship.setCreatedAt(OffsetDateTime.now());
        mainRelationship.setUpdatedAt(OffsetDateTime.now());

        mainRelationship = relationshipRepository.save(mainRelationship);

        if (!relType.isBidirectional() && relType.getReverseTypeId() != null) {
            RelationshipType reverseType =
                    relationshipTypeRepository
                            .findById(relType.getReverseTypeId())
                            .orElseThrow(ReverseRelationshipTypeNotFoundException::new);

            if (reverseType.isOnly()) {
                Optional<Relationship> reverseExisting =
                        relationshipRepository
                                .findFirstByFromUserProfileIdAndRelationshipTypeIdAndStatus(
                                        toProfile.getIndividualId(),
                                        reverseType.getRelaTypeId(),
                                        STATUS_ACTIVE);
                if (reverseExisting.isPresent()) {
                    throw new ReverseRelationshipOnlyConstraintViolatedException();
                }
            }

            Relationship reverseRelationship = new Relationship();
            reverseRelationship.setFromUserProfileId(toProfile.getIndividualId());
            reverseRelationship.setToUserProfileId(fromProfile.getIndividualId());
            reverseRelationship.setRelationshipTypeId(reverseType.getRelaTypeId());
            reverseRelationship.setStatus(STATUS_ACTIVE);
            reverseRelationship.setStartedAt(startedAt);
            reverseRelationship.setCreatedAt(OffsetDateTime.now());
            reverseRelationship.setUpdatedAt(OffsetDateTime.now());

            relationshipRepository.save(reverseRelationship);
        }

        return mainRelationship.getRelationshipId();
    }

    public static class CreateRelationshipCommand {
        private Long fromProfileId;
        private Long toProfileId;
        private Long relationshipTypeId;
        private OffsetDateTime startedAt;

        public Long getFromProfileId() {
            return fromProfileId;
        }

        public void setFromProfileId(Long fromProfileId) {
            this.fromProfileId = fromProfileId;
        }

        public Long getToProfileId() {
            return toProfileId;
        }

        public void setToProfileId(Long toProfileId) {
            this.toProfileId = toProfileId;
        }

        public Long getRelationshipTypeId() {
            return relationshipTypeId;
        }

        public void setRelationshipTypeId(Long relationshipTypeId) {
            this.relationshipTypeId = relationshipTypeId;
        }

        public OffsetDateTime getStartedAt() {
            return startedAt;
        }

        public void setStartedAt(OffsetDateTime startedAt) {
            this.startedAt = startedAt;
        }
    }

    public static class ProfileNotFoundException extends RuntimeException {}

    public static class InvalidSelfRelationshipException extends RuntimeException {}

    public static class RelationshipTypeNotFoundException extends RuntimeException {}

    public static class RelationshipTypeOnlyConstraintViolatedException
            extends RuntimeException {}

    public static class ReverseRelationshipTypeNotFoundException extends RuntimeException {}

    public static class ReverseRelationshipOnlyConstraintViolatedException
            extends RuntimeException {}
}
