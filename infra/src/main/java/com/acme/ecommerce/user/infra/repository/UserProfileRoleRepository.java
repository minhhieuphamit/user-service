package com.acme.ecommerce.user.infra.repository;

import com.acme.ecommerce.user.core.domain.entity.UserProfileRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRoleRepository extends JpaRepository<UserProfileRole, Long> {

    List<UserProfileRole> findByUserProfileId(Long userProfileId);

    List<UserProfileRole> findByUserId(String userId);

    Optional<UserProfileRole> findByUserProfileIdAndRoleId(Long userProfileId, Long roleId);

    boolean existsByUserProfileIdAndRoleId(Long userProfileId, Long roleId);

    void deleteByUserProfileId(Long userProfileId);
}
