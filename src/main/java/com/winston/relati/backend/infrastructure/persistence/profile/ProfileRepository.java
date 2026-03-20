package com.winston.relati.backend.infrastructure.persistence.profile;

import com.winston.relati.backend.domain.model.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
