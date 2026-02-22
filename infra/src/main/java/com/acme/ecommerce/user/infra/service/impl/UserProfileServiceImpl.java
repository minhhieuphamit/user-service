package com.acme.ecommerce.user.infra.service.impl;

import com.acme.ecommerce.user.core.businessexception.ErrorCode;
import com.acme.ecommerce.user.core.domain.common.PagedResponse;
import com.acme.ecommerce.user.core.domain.entity.UserProfile;
import com.acme.ecommerce.user.core.domain.entity.UserProfileRole;
import com.acme.ecommerce.user.core.domain.entity.UserRole;
import com.acme.ecommerce.user.core.domain.request.AssignRoleRequestDTO;
import com.acme.ecommerce.user.core.domain.request.CreateUserRequestDTO;
import com.acme.ecommerce.user.core.domain.request.UpdateUserRequestDTO;
import com.acme.ecommerce.user.core.domain.response.UserProfileResponse;
import com.acme.ecommerce.user.core.service.UserProfileService;
import com.acme.ecommerce.user.infra.repository.UserProfileRepository;
import com.acme.ecommerce.user.infra.repository.UserProfileRoleRepository;
import com.acme.ecommerce.user.infra.repository.UserRoleRepository;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserProfileRoleRepository userProfileRoleRepository;

    @Override
    @Transactional
    public Either<ErrorCode, UserProfileResponse> createUser(CreateUserRequestDTO request) {
        try {
            log.info("createUser start, request: [userId: {}]", request.getUserId());

            if (userProfileRepository.existsByUserId(request.getUserId())) {
                log.warn("User already exists: [userId: {}]", request.getUserId());
                return Either.left(ErrorCode.ALREADY_EXISTS);
            }

            var userProfile = UserProfile.builder()
                    .userId(request.getUserId())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .status(true)
                    .build();

            userProfile = userProfileRepository.save(userProfile);
            log.info("createUser success, id: [{}]", userProfile.getId());

            return Either.right(toResponse(userProfile, Collections.emptyList()));
        } catch (Exception e) {
            log.error("createUser exception: [{}]", e.getMessage(), e);
            return Either.left(ErrorCode.SERVER_ERROR);
        } finally {
            log.info("createUser end");
        }
    }

    @Override
    public Either<ErrorCode, UserProfileResponse> getUserByUserId(String userId) {
        try {
            log.info("getUserByUserId start, userId: [{}]", userId);

            var userProfileOpt = userProfileRepository.findByUserId(userId);
            if (userProfileOpt.isEmpty()) {
                log.warn("User not found: [userId: {}]", userId);
                return Either.left(ErrorCode.NOT_FOUND);
            }

            var userProfile = userProfileOpt.get();
            var roles = getRoleCodes(userProfile.getId());

            return Either.right(toResponse(userProfile, roles));
        } catch (Exception e) {
            log.error("getUserByUserId exception: [{}]", e.getMessage(), e);
            return Either.left(ErrorCode.SERVER_ERROR);
        } finally {
            log.info("getUserByUserId end");
        }
    }

    @Override
    public Either<ErrorCode, PagedResponse<UserProfileResponse>> getAllUsers(int page, int size) {
        try {
            log.info("getAllUsers start, page: [{}], size: [{}]", page, size);

            var pageable = PageRequest.of(page, size);
            var userPage = userProfileRepository.findAll(pageable);
            var responseList = userPage.getContent().stream()
                    .map(user -> toResponse(user, getRoleCodes(user.getId())))
                    .toList();

            var pagedResponse = PagedResponse.<UserProfileResponse>builder()
                    .content(responseList)
                    .page(userPage.getNumber())
                    .size(userPage.getSize())
                    .totalElements(userPage.getTotalElements())
                    .totalPages(userPage.getTotalPages())
                    .build();

            return Either.right(pagedResponse);
        } catch (Exception e) {
            log.error("getAllUsers exception: [{}]", e.getMessage(), e);
            return Either.left(ErrorCode.SERVER_ERROR);
        } finally {
            log.info("getAllUsers end");
        }
    }

    @Override
    @Transactional
    public Either<ErrorCode, UserProfileResponse> updateUser(String userId, UpdateUserRequestDTO request) {
        try {
            log.info("updateUser start, userId: [{}]", userId);

            var userProfileOpt = userProfileRepository.findByUserId(userId);
            if (userProfileOpt.isEmpty()) {
                log.warn("User not found: [userId: {}]", userId);
                return Either.left(ErrorCode.NOT_FOUND);
            }

            var userProfile = userProfileOpt.get();

            if (request.getEmail() != null) {
                userProfile.setEmail(request.getEmail());
            }
            if (request.getPhoneNumber() != null) {
                userProfile.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getStatus() != null) {
                userProfile.setStatus(request.getStatus());
            }

            userProfile = userProfileRepository.save(userProfile);
            var roles = getRoleCodes(userProfile.getId());
            log.info("updateUser success, userId: [{}]", userId);

            return Either.right(toResponse(userProfile, roles));
        } catch (Exception e) {
            log.error("updateUser exception: [{}]", e.getMessage(), e);
            return Either.left(ErrorCode.SERVER_ERROR);
        } finally {
            log.info("updateUser end");
        }
    }

    @Override
    @Transactional
    public Either<ErrorCode, Void> deleteUser(String userId) {
        try {
            log.info("deleteUser (soft) start, userId: [{}]", userId);

            var userProfileOpt = userProfileRepository.findByUserId(userId);
            if (userProfileOpt.isEmpty()) {
                log.warn("User not found: [userId: {}]", userId);
                return Either.left(ErrorCode.NOT_FOUND);
            }

            var userProfile = userProfileOpt.get();
            userProfile.setStatus(false);
            userProfileRepository.save(userProfile);
            log.info("deleteUser (soft) success, userId: [{}]", userId);

            return Either.right(null);
        } catch (Exception e) {
            log.error("deleteUser exception: [{}]", e.getMessage(), e);
            return Either.left(ErrorCode.SERVER_ERROR);
        } finally {
            log.info("deleteUser end");
        }
    }

    @Override
    @Transactional
    public Either<ErrorCode, Void> hardDeleteUser(String userId) {
        try {
            log.info("hardDeleteUser start, userId: [{}]", userId);

            var userProfileOpt = userProfileRepository.findByUserId(userId);
            if (userProfileOpt.isEmpty()) {
                log.warn("User not found: [userId: {}]", userId);
                return Either.left(ErrorCode.NOT_FOUND);
            }

            var userProfile = userProfileOpt.get();
            userProfileRoleRepository.deleteByUserProfileId(userProfile.getId());
            userProfileRepository.delete(userProfile);
            log.info("hardDeleteUser success, userId: [{}]", userId);

            return Either.right(null);
        } catch (Exception e) {
            log.error("hardDeleteUser exception: [{}]", e.getMessage(), e);
            return Either.left(ErrorCode.SERVER_ERROR);
        } finally {
            log.info("hardDeleteUser end");
        }
    }

    @Override
    @Transactional
    public Either<ErrorCode, UserProfileResponse> assignRole(String userId, AssignRoleRequestDTO request) {
        try {
            log.info("assignRole start, userId: [{}], roleCode: [{}]", userId, request.getRoleCode());

            var userProfileOpt = userProfileRepository.findByUserId(userId);
            if (userProfileOpt.isEmpty()) {
                log.warn("User not found: [userId: {}]", userId);
                return Either.left(ErrorCode.NOT_FOUND);
            }

            var roleOpt = userRoleRepository.findByRoleCode(request.getRoleCode());
            if (roleOpt.isEmpty()) {
                log.warn("Role not found: [roleCode: {}]", request.getRoleCode());
                return Either.left(ErrorCode.ROLE_NOT_FOUND);
            }

            var userProfile = userProfileOpt.get();
            var role = roleOpt.get();

            if (userProfileRoleRepository.existsByUserProfileIdAndRoleId(userProfile.getId(), role.getId())) {
                log.warn("Role already assigned: [userId: {}, roleCode: {}]", userId, request.getRoleCode());
                return Either.left(ErrorCode.ROLE_ALREADY_ASSIGNED);
            }

            var userProfileRole = UserProfileRole.builder()
                    .userProfileId(userProfile.getId())
                    .roleId(role.getId())
                    .userId(userId)
                    .roleCode(request.getRoleCode())
                    .build();

            userProfileRoleRepository.save(userProfileRole);
            var roles = getRoleCodes(userProfile.getId());
            log.info("assignRole success, userId: [{}], roleCode: [{}]", userId, request.getRoleCode());

            return Either.right(toResponse(userProfile, roles));
        } catch (Exception e) {
            log.error("assignRole exception: [{}]", e.getMessage(), e);
            return Either.left(ErrorCode.SERVER_ERROR);
        } finally {
            log.info("assignRole end");
        }
    }

    @Override
    @Transactional
    public Either<ErrorCode, UserProfileResponse> removeRole(String userId, String roleCode) {
        try {
            log.info("removeRole start, userId: [{}], roleCode: [{}]", userId, roleCode);

            var userProfileOpt = userProfileRepository.findByUserId(userId);
            if (userProfileOpt.isEmpty()) {
                log.warn("User not found: [userId: {}]", userId);
                return Either.left(ErrorCode.NOT_FOUND);
            }

            var roleOpt = userRoleRepository.findByRoleCode(roleCode);
            if (roleOpt.isEmpty()) {
                log.warn("Role not found: [roleCode: {}]", roleCode);
                return Either.left(ErrorCode.ROLE_NOT_FOUND);
            }

            var userProfile = userProfileOpt.get();
            var role = roleOpt.get();

            var mappingOpt = userProfileRoleRepository.findByUserProfileIdAndRoleId(userProfile.getId(), role.getId());
            if (mappingOpt.isEmpty()) {
                log.warn("Role not assigned: [userId: {}, roleCode: {}]", userId, roleCode);
                return Either.left(ErrorCode.ROLE_NOT_ASSIGNED);
            }

            userProfileRoleRepository.delete(mappingOpt.get());
            var roles = getRoleCodes(userProfile.getId());
            log.info("removeRole success, userId: [{}], roleCode: [{}]", userId, roleCode);

            return Either.right(toResponse(userProfile, roles));
        } catch (Exception e) {
            log.error("removeRole exception: [{}]", e.getMessage(), e);
            return Either.left(ErrorCode.SERVER_ERROR);
        } finally {
            log.info("removeRole end");
        }
    }

    private List<String> getRoleCodes(Long userProfileId) {
        return userProfileRoleRepository.findByUserProfileId(userProfileId)
                .stream()
                .map(UserProfileRole::getRoleCode)
                .toList();
    }

    private UserProfileResponse toResponse(UserProfile userProfile, List<String> roles) {
        return UserProfileResponse.builder()
                .id(userProfile.getId())
                .userId(userProfile.getUserId())
                .status(userProfile.getStatus())
                .email(userProfile.getEmail())
                .phoneNumber(userProfile.getPhoneNumber())
                .roles(roles)
                .createdDate(userProfile.getCreatedDate())
                .updatedDate(userProfile.getUpdatedDate())
                .build();
    }
}
