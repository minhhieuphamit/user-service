package com.acme.ecommerce.user.api.controller.external;

import com.acme.ecommerce.user.api.domain.mapper.EitherMapper;
import com.acme.ecommerce.user.api.utils.ResponseApi;
import com.acme.ecommerce.user.core.domain.request.AssignRoleRequestDTO;
import com.acme.ecommerce.user.core.domain.request.CreateUserRequestDTO;
import com.acme.ecommerce.user.core.domain.request.UpdateUserRequestDTO;
import com.acme.ecommerce.user.core.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Profile", description = "CRUD APIs for User Profile management")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping
    @Operation(operationId = "createUser", summary = "Create user", description = "Create a new user profile")
    public ResponseEntity<ResponseApi> createUser(@RequestBody @Validated CreateUserRequestDTO request) {
        log.info("createUser start, request: [userId: {}]", request.getUserId());
        try {
            return EitherMapper.eitherMapper(userProfileService.createUser(request));
        } finally {
            log.info("createUser end");
        }
    }

    @GetMapping("/{userId}")
    @Operation(operationId = "getUserByUserId", summary = "Get user by userId", description = "Retrieve user profile by userId")
    public ResponseEntity<ResponseApi> getUserByUserId(
            @Parameter(description = "User ID") @PathVariable String userId) {
        log.info("getUserByUserId start, userId: [{}]", userId);
        try {
            return EitherMapper.eitherMapper(userProfileService.getUserByUserId(userId));
        } finally {
            log.info("getUserByUserId end");
        }
    }

    @GetMapping
    @Operation(operationId = "getAllUsers", summary = "Get all users", description = "Retrieve all user profiles")
    public ResponseEntity<ResponseApi> getAllUsers() {
        log.info("getAllUsers start");
        try {
            return EitherMapper.eitherMapper(userProfileService.getAllUsers());
        } finally {
            log.info("getAllUsers end");
        }
    }

    @PutMapping("/{userId}")
    @Operation(operationId = "updateUser", summary = "Update user", description = "Update user profile (email, phone, status)")
    public ResponseEntity<ResponseApi> updateUser(
            @Parameter(description = "User ID") @PathVariable String userId,
            @RequestBody @Validated UpdateUserRequestDTO request) {
        log.info("updateUser start, userId: [{}]", userId);
        try {
            return EitherMapper.eitherMapper(userProfileService.updateUser(userId, request));
        } finally {
            log.info("updateUser end");
        }
    }

    @DeleteMapping("/{userId}")
    @Operation(operationId = "softDeleteUser", summary = "Soft delete user", description = "Set user status to false (inactive). User can be reactivated later.")
    public ResponseEntity<ResponseApi> deleteUser(
            @Parameter(description = "User ID") @PathVariable String userId) {
        log.info("deleteUser (soft) start, userId: [{}]", userId);
        try {
            return EitherMapper.eitherMapper(userProfileService.deleteUser(userId));
        } finally {
            log.info("deleteUser end");
        }
    }

    @DeleteMapping("/{userId}/hard")
    @Operation(operationId = "hardDeleteUser", summary = "Hard delete user", description = "Permanently remove user and all role mappings from database. This action cannot be undone.")
    public ResponseEntity<ResponseApi> hardDeleteUser(
            @Parameter(description = "User ID") @PathVariable String userId) {
        log.info("hardDeleteUser start, userId: [{}]", userId);
        try {
            return EitherMapper.eitherMapper(userProfileService.hardDeleteUser(userId));
        } finally {
            log.info("hardDeleteUser end");
        }
    }

    @PostMapping("/{userId}/roles")
    @Operation(operationId = "assignRoleToUser", summary = "Assign role to user", description = "Assign a role to user by roleCode")
    public ResponseEntity<ResponseApi> assignRole(
            @Parameter(description = "User ID") @PathVariable String userId,
            @RequestBody @Validated AssignRoleRequestDTO request) {
        log.info("assignRole start, userId: [{}], roleCode: [{}]", userId, request.getRoleCode());
        try {
            return EitherMapper.eitherMapper(userProfileService.assignRole(userId, request));
        } finally {
            log.info("assignRole end");
        }
    }

    @DeleteMapping("/{userId}/roles/{roleCode}")
    @Operation(operationId = "removeRoleFromUser", summary = "Remove role from user", description = "Remove a role assignment from user")
    public ResponseEntity<ResponseApi> removeRole(
            @Parameter(description = "User ID") @PathVariable String userId,
            @Parameter(description = "Role code") @PathVariable String roleCode) {
        log.info("removeRole start, userId: [{}], roleCode: [{}]", userId, roleCode);
        try {
            return EitherMapper.eitherMapper(userProfileService.removeRole(userId, roleCode));
        } finally {
            log.info("removeRole end");
        }
    }
}
