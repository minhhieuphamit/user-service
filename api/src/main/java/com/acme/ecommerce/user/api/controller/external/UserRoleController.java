package com.acme.ecommerce.user.api.controller.external;

import com.acme.ecommerce.user.api.domain.mapper.EitherMapper;
import com.acme.ecommerce.user.api.utils.ResponseApi;
import com.acme.ecommerce.user.core.domain.request.CreateRoleRequestDTO;
import com.acme.ecommerce.user.core.service.UserRoleService;
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
@RequestMapping("/v1/roles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Roles", description = "CRUD APIs for User Roles management")
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping
    @Operation(operationId = "createRole", summary = "Create role", description = "Create a new role")
    public ResponseEntity<ResponseApi> createRole(@RequestBody @Validated CreateRoleRequestDTO request) {
        log.info("createRole start, roleCode: [{}]", request.getRoleCode());
        try {
            return EitherMapper.eitherMapper(userRoleService.createRole(request));
        } finally {
            log.info("createRole end");
        }
    }

    @GetMapping("/{roleCode}")
    @Operation(operationId = "getRoleByCode", summary = "Get role by code", description = "Retrieve role by roleCode")
    public ResponseEntity<ResponseApi> getRoleByCode(
            @Parameter(description = "Role code") @PathVariable String roleCode) {
        log.info("getRoleByCode start, roleCode: [{}]", roleCode);
        try {
            return EitherMapper.eitherMapper(userRoleService.getRoleByCode(roleCode));
        } finally {
            log.info("getRoleByCode end");
        }
    }

    @GetMapping
    @Operation(operationId = "getAllRoles", summary = "Get all roles", description = "Retrieve all roles")
    public ResponseEntity<ResponseApi> getAllRoles() {
        log.info("getAllRoles start");
        try {
            return EitherMapper.eitherMapper(userRoleService.getAllRoles());
        } finally {
            log.info("getAllRoles end");
        }
    }

    @PutMapping("/{roleCode}")
    @Operation(operationId = "updateRole", summary = "Update role", description = "Update role name by roleCode")
    public ResponseEntity<ResponseApi> updateRole(
            @Parameter(description = "Role code") @PathVariable String roleCode,
            @RequestBody @Validated CreateRoleRequestDTO request) {
        log.info("updateRole start, roleCode: [{}]", roleCode);
        try {
            return EitherMapper.eitherMapper(userRoleService.updateRole(roleCode, request));
        } finally {
            log.info("updateRole end");
        }
    }

    @DeleteMapping("/{roleCode}")
    @Operation(operationId = "softDeleteRole", summary = "Soft delete role", description = "Set role status to false (inactive). Role can be reactivated later.")
    public ResponseEntity<ResponseApi> deleteRole(
            @Parameter(description = "Role code") @PathVariable String roleCode) {
        log.info("deleteRole (soft) start, roleCode: [{}]", roleCode);
        try {
            return EitherMapper.eitherMapper(userRoleService.deleteRole(roleCode));
        } finally {
            log.info("deleteRole end");
        }
    }

    @DeleteMapping("/{roleCode}/hard")
    @Operation(operationId = "hardDeleteRole", summary = "Hard delete role", description = "Permanently remove role from database. This action cannot be undone.")
    public ResponseEntity<ResponseApi> hardDeleteRole(
            @Parameter(description = "Role code") @PathVariable String roleCode) {
        log.info("hardDeleteRole start, roleCode: [{}]", roleCode);
        try {
            return EitherMapper.eitherMapper(userRoleService.hardDeleteRole(roleCode));
        } finally {
            log.info("hardDeleteRole end");
        }
    }
}
