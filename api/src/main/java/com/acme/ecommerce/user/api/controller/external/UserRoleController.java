package com.acme.ecommerce.user.api.controller.external;

import com.acme.ecommerce.user.api.domain.mapper.EitherMapper;
import com.acme.ecommerce.user.api.utils.ResponseApi;
import com.acme.ecommerce.user.core.domain.request.CreateRoleRequestDTO;
import com.acme.ecommerce.user.core.service.UserRoleService;
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
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping
    public ResponseEntity<ResponseApi> createRole(@RequestBody @Validated CreateRoleRequestDTO request) {
        log.info("createRole start, roleCode: [{}]", request.getRoleCode());
        try {
            return EitherMapper.eitherMapper(userRoleService.createRole(request));
        } finally {
            log.info("createRole end");
        }
    }

    @GetMapping("/{roleCode}")
    public ResponseEntity<ResponseApi> getRoleByCode(@PathVariable String roleCode) {
        log.info("getRoleByCode start, roleCode: [{}]", roleCode);
        try {
            return EitherMapper.eitherMapper(userRoleService.getRoleByCode(roleCode));
        } finally {
            log.info("getRoleByCode end");
        }
    }

    @GetMapping
    public ResponseEntity<ResponseApi> getAllRoles() {
        log.info("getAllRoles start");
        try {
            return EitherMapper.eitherMapper(userRoleService.getAllRoles());
        } finally {
            log.info("getAllRoles end");
        }
    }

    @PutMapping("/{roleCode}")
    public ResponseEntity<ResponseApi> updateRole(
            @PathVariable String roleCode,
            @RequestBody @Validated CreateRoleRequestDTO request) {
        log.info("updateRole start, roleCode: [{}]", roleCode);
        try {
            return EitherMapper.eitherMapper(userRoleService.updateRole(roleCode, request));
        } finally {
            log.info("updateRole end");
        }
    }

    @DeleteMapping("/{roleCode}")
    public ResponseEntity<ResponseApi> deleteRole(@PathVariable String roleCode) {
        log.info("deleteRole start, roleCode: [{}]", roleCode);
        try {
            return EitherMapper.eitherMapper(userRoleService.deleteRole(roleCode));
        } finally {
            log.info("deleteRole end");
        }
    }
}
