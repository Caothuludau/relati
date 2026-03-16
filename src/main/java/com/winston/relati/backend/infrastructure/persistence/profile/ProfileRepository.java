package com.winston.relati.backend.infrastructure.persistence.profile;

import com.winston.relati.backend.domain.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
