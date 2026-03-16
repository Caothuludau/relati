package com.winston.relati.backend.application.relationship;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import com.winston.relati.backend.domain.profile.Profile;
import com.winston.relati.backend.domain.relationship.Relationship;
import com.winston.relati.backend.domain.relationship.RelationshipType;
import com.winston.relati.backend.infrastructure.persistence.profile.ProfileRepository;
import com.winston.relati.backend.infrastructure.persistence.relationship.RelationshipRepository;
import com.winston.relati.backend.infrastructure.persistence.relationship.RelationshipTypeRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CreateRelationshipServiceTest {

    private ProfileRepository profileRepository;
    private RelationshipRepository relationshipRepository;
    private RelationshipTypeRepository relationshipTypeRepository;

    private CreateRelationshipService service;

    @BeforeEach
    void setUp() {
        profileRepository = Mockito.mock(ProfileRepository.class);
        relationshipRepository = Mockito.mock(RelationshipRepository.class);
        relationshipTypeRepository = Mockito.mock(RelationshipTypeRepository.class);

        service =
                new CreateRelationshipService(
                        profileRepository, relationshipRepository, relationshipTypeRepository);
    }

    @Test
    void createRelationship_success_bidirectional() {
        CreateRelationshipCommand command = new CreateRelationshipCommand();
        command.setFromProfileId(1L);
        command.setToProfileId(2L);
        command.setRelationshipTypeId(10L);

        Profile from = new Profile();
        from.setIndividualId(1L);
        Profile to = new Profile();
        to.setIndividualId(2L);

        RelationshipType type = new RelationshipType();
        type.setRelaTypeId(10L);
        type.setBidirectional(true);
        type.setOnly(false);

        Relationship saved = new Relationship();
        saved.setRelationshipId(100L);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(from));
        when(profileRepository.findById(2L)).thenReturn(Optional.of(to));
        when(relationshipTypeRepository.findById(10L)).thenReturn(Optional.of(type));
        when(relationshipRepository
                        .findFirstByFromUserProfileIdAndRelationshipTypeIdAndStatus(
                                any(), any(), any()))
                .thenReturn(Optional.empty());
        when(relationshipRepository.save(any(Relationship.class))).thenReturn(saved);

        Long id = service.createRelationship(command);

        assertThat(id).isEqualTo(100L);
    }

    @Test
    void createRelationship_rejects_whenProfileMissing() {
        CreateRelationshipCommand command = new CreateRelationshipCommand();
        command.setFromProfileId(1L);
        command.setToProfileId(2L);
        command.setRelationshipTypeId(10L);

        when(profileRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createRelationship(command))
                .isInstanceOf(ProfileNotFoundException.class);
    }

    @Test
    void createRelationship_rejects_selfRelationship() {
        CreateRelationshipCommand command = new CreateRelationshipCommand();
        command.setFromProfileId(1L);
        command.setToProfileId(1L);
        command.setRelationshipTypeId(10L);

        Profile profile = new Profile();
        profile.setIndividualId(1L);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        assertThatThrownBy(() -> service.createRelationship(command))
                .isInstanceOf(InvalidSelfRelationshipException.class);
    }

    @Test
    void createRelationship_rejects_whenRelationshipTypeMissing() {
        CreateRelationshipCommand command = new CreateRelationshipCommand();
        command.setFromProfileId(1L);
        command.setToProfileId(2L);
        command.setRelationshipTypeId(10L);

        Profile from = new Profile();
        from.setIndividualId(1L);
        Profile to = new Profile();
        to.setIndividualId(2L);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(from));
        when(profileRepository.findById(2L)).thenReturn(Optional.of(to));
        when(relationshipTypeRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createRelationship(command))
                .isInstanceOf(RelationshipTypeNotFoundException.class);
    }

    @Test
    void createRelationship_rejects_whenIsOnlyViolated() {
        CreateRelationshipCommand command = new CreateRelationshipCommand();
        command.setFromProfileId(1L);
        command.setToProfileId(2L);
        command.setRelationshipTypeId(10L);

        Profile from = new Profile();
        from.setIndividualId(1L);
        Profile to = new Profile();
        to.setIndividualId(2L);

        RelationshipType type = new RelationshipType();
        type.setRelaTypeId(10L);
        type.setOnly(true);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(from));
        when(profileRepository.findById(2L)).thenReturn(Optional.of(to));
        when(relationshipTypeRepository.findById(10L)).thenReturn(Optional.of(type));
        when(relationshipRepository
                        .findFirstByFromUserProfileIdAndRelationshipTypeIdAndStatus(
                                any(), any(), any()))
                .thenReturn(Optional.of(new Relationship()));

        assertThatThrownBy(() -> service.createRelationship(command))
                .isInstanceOf(RelationshipTypeOnlyConstraintViolatedException.class);
    }

    @Test
    void createRelationship_rejects_whenReverseIsOnlyViolated() {
        CreateRelationshipCommand command = new CreateRelationshipCommand();
        command.setFromProfileId(1L);
        command.setToProfileId(2L);
        command.setRelationshipTypeId(10L);

        Profile from = new Profile();
        from.setIndividualId(1L);
        Profile to = new Profile();
        to.setIndividualId(2L);

        RelationshipType type = new RelationshipType();
        type.setRelaTypeId(10L);
        type.setBidirectional(false);
        type.setOnly(false);
        type.setReverseTypeId(20L);

        RelationshipType reverseType = new RelationshipType();
        reverseType.setRelaTypeId(20L);
        reverseType.setOnly(true);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(from));
        when(profileRepository.findById(2L)).thenReturn(Optional.of(to));
        when(relationshipTypeRepository.findById(10L)).thenReturn(Optional.of(type));
        when(relationshipTypeRepository.findById(20L)).thenReturn(Optional.of(reverseType));
        when(relationshipRepository
                        .findFirstByFromUserProfileIdAndRelationshipTypeIdAndStatus(
                                from.getIndividualId(), type.getRelaTypeId(), "ACTIVE"))
                .thenReturn(Optional.empty());
        when(relationshipRepository
                        .findFirstByFromUserProfileIdAndRelationshipTypeIdAndStatus(
                                to.getIndividualId(), reverseType.getRelaTypeId(), "ACTIVE"))
                .thenReturn(Optional.of(new Relationship()));

        assertThatThrownBy(() -> service.createRelationship(command))
                .isInstanceOf(ReverseRelationshipOnlyConstraintViolatedException.class);
    }
}
