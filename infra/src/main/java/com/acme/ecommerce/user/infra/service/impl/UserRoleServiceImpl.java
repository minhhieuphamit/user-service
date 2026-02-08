package com.acme.ecommerce.user.infra.service.impl;

import com.acme.ecommerce.user.core.businessexception.ErrorCode;
import com.acme.ecommerce.user.core.domain.entity.UserRole;
import com.acme.ecommerce.user.core.domain.request.CreateRoleRequestDTO;
import com.acme.ecommerce.user.core.domain.response.UserRoleResponse;
import com.acme.ecommerce.user.core.service.UserRoleService;
import com.acme.ecommerce.user.infra.repository.UserRoleRepository;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public Either<ErrorCode, UserRoleResponse> createRole(CreateRoleRequestDTO request) {
        try {
            log.info("createRole start, roleCode: [{}]", request.getRoleCode());

            if (userRoleRepository.existsByRoleCode(request.getRoleCode())) {
                log.warn("Role already exists: [roleCode: {}]", request.getRoleCode());
                return Either.left(ErrorCode.ROLE_ALREADY_EXISTS);
            }

            var role = UserRole.builder()
                    .roleCode(request.getRoleCode())
                    .roleName(request.getRoleName())
                    .status(true)
                    .build();

            role = userRoleRepository.save(role);
            log.info("createRole success, id: [{}]", role.getId());

            return Either.right(toResponse(role));
        } catch (Exception e) {
            log.error("createRole exception: [{}]", e.getMessage(), e);
            return Either.left(ErrorCode.SERVER_ERROR);
        } finally {
            log.info("createRole end");
        }
    }

    @Override
    public Either<ErrorCode, UserRoleResponse> getRoleByCode(String roleCode) {
        try {
            log.info("getRoleByCode start, roleCode: [{}]", roleCode);

            var roleOpt = userRoleRepository.findByRoleCode(roleCode);
            if (roleOpt.isEmpty()) {
                log.warn("Role not found: [roleCode: {}]", roleCode);
                return Either.left(ErrorCode.ROLE_NOT_FOUND);
            }

            return Either.right(toResponse(roleOpt.get()));
        } catch (Exception e) {
            log.error("getRoleByCode exception: [{}]", e.getMessage(), e);
            return Either.left(ErrorCode.SERVER_ERROR);
        } finally {
            log.info("getRoleByCode end");
        }
    }

    @Override
    public Either<ErrorCode, List<UserRoleResponse>> getAllRoles() {
        try {
            log.info("getAllRoles start");

            var roles = userRoleRepository.findAll();
            var responseList = roles.stream()
                    .map(this::toResponse)
                    .toList();

            return Either.right(responseList);
        } catch (Exception e) {
            log.error("getAllRoles exception: [{}]", e.getMessage(), e);
            return Either.left(ErrorCode.SERVER_ERROR);
        } finally {
            log.info("getAllRoles end");
        }
    }

    @Override
    @Transactional
    public Either<ErrorCode, UserRoleResponse> updateRole(String roleCode, CreateRoleRequestDTO request) {
        try {
            log.info("updateRole start, roleCode: [{}]", roleCode);

            var roleOpt = userRoleRepository.findByRoleCode(roleCode);
            if (roleOpt.isEmpty()) {
                log.warn("Role not found: [roleCode: {}]", roleCode);
                return Either.left(ErrorCode.ROLE_NOT_FOUND);
            }

            var role = roleOpt.get();
            role.setRoleName(request.getRoleName());
            role = userRoleRepository.save(role);
            log.info("updateRole success, roleCode: [{}]", roleCode);

            return Either.right(toResponse(role));
        } catch (Exception e) {
            log.error("updateRole exception: [{}]", e.getMessage(), e);
            return Either.left(ErrorCode.SERVER_ERROR);
        } finally {
            log.info("updateRole end");
        }
    }

    @Override
    @Transactional
    public Either<ErrorCode, Void> deleteRole(String roleCode) {
        try {
            log.info("deleteRole start, roleCode: [{}]", roleCode);

            var roleOpt = userRoleRepository.findByRoleCode(roleCode);
            if (roleOpt.isEmpty()) {
                log.warn("Role not found: [roleCode: {}]", roleCode);
                return Either.left(ErrorCode.ROLE_NOT_FOUND);
            }

            userRoleRepository.delete(roleOpt.get());
            log.info("deleteRole success, roleCode: [{}]", roleCode);

            return Either.right(null);
        } catch (Exception e) {
            log.error("deleteRole exception: [{}]", e.getMessage(), e);
            return Either.left(ErrorCode.SERVER_ERROR);
        } finally {
            log.info("deleteRole end");
        }
    }

    private UserRoleResponse toResponse(UserRole role) {
        return UserRoleResponse.builder()
                .id(role.getId())
                .roleCode(role.getRoleCode())
                .roleName(role.getRoleName())
                .status(role.getStatus())
                .createdDate(role.getCreatedDate())
                .updatedDate(role.getUpdatedDate())
                .build();
    }
}
