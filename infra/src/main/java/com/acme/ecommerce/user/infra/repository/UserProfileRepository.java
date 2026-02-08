package com.acme.ecommerce.user.infra.repository;

import com.acme.ecommerce.user.core.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserId(String userId);

    boolean existsByUserId(String userId);
}
