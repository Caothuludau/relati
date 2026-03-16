package com.winston.relati.backend.api.relationship;

import com.winston.relati.backend.application.relationship.CreateRelationshipService;
import com.winston.relati.backend.application.relationship.CreateRelationshipService.CreateRelationshipCommand;
import com.winston.relati.backend.application.relationship.CreateRelationshipService
        .InvalidSelfRelationshipException;
import com.winston.relati.backend.application.relationship.CreateRelationshipService
        .ProfileNotFoundException;
import com.winston.relati.backend.application.relationship.CreateRelationshipService
        .RelationshipTypeNotFoundException;
import com.winston.relati.backend.application.relationship.CreateRelationshipService
        .RelationshipTypeOnlyConstraintViolatedException;
import com.winston.relati.backend.application.relationship.CreateRelationshipService
        .ReverseRelationshipOnlyConstraintViolatedException;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relationships")
public class RelationshipController {

    private final CreateRelationshipService createRelationshipService;

    public RelationshipController(CreateRelationshipService createRelationshipService) {
        this.createRelationshipService = createRelationshipService;
    }

    @PostMapping
    public ResponseEntity<CreateRelationshipResponse> createRelationship(
            @RequestBody CreateRelationshipRequest request) {
        CreateRelationshipCommand command = new CreateRelationshipCommand();
        command.setFromProfileId(request.getFromProfileId());
        command.setToProfileId(request.getToProfileId());
        command.setRelationshipTypeId(request.getRelationshipTypeId());
        command.setStartedAt(request.getStartedAt());

        Long id = createRelationshipService.createRelationship(command);
        CreateRelationshipResponse response = new CreateRelationshipResponse();
        response.setRelationshipId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProfileNotFound() {
        return error("PROFILE_NOT_FOUND", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RelationshipTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRelationshipTypeNotFound() {
        return error("RELATIONSHIP_TYPE_NOT_FOUND", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSelfRelationshipException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSelfRelationship() {
        return error("INVALID_SELF_RELATIONSHIP", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RelationshipTypeOnlyConstraintViolatedException.class)
    public ResponseEntity<ErrorResponse> handleTypeOnlyViolation() {
        return error("RELATIONSHIP_TYPE_ONLY_VIOLATION", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReverseRelationshipOnlyConstraintViolatedException.class)
    public ResponseEntity<ErrorResponse> handleReverseTypeOnlyViolation() {
        return error("REVERSE_RELATIONSHIP_ONLY_VIOLATION", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> error(String code, HttpStatus status) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(code);
        return ResponseEntity.status(status).body(response);
    }

    public static class CreateRelationshipRequest {
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

    public static class CreateRelationshipResponse {
        private Long relationshipId;

        public Long getRelationshipId() {
            return relationshipId;
        }

        public void setRelationshipId(Long relationshipId) {
            this.relationshipId = relationshipId;
        }
    }

    public static class ErrorResponse {
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
