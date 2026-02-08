package com.acme.ecommerce.user.api.controller.external;

import com.acme.ecommerce.user.api.domain.mapper.EitherMapper;
import com.acme.ecommerce.user.api.utils.ResponseApi;
import com.acme.ecommerce.user.core.domain.request.AssignRoleRequestDTO;
import com.acme.ecommerce.user.core.domain.request.CreateUserRequestDTO;
import com.acme.ecommerce.user.core.domain.request.UpdateUserRequestDTO;
import com.acme.ecommerce.user.core.service.UserProfileService;
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
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<ResponseApi> createUser(@RequestBody @Validated CreateUserRequestDTO request) {
        log.info("createUser start, request: [userId: {}]", request.getUserId());
        try {
            return EitherMapper.eitherMapper(userProfileService.createUser(request));
        } finally {
            log.info("createUser end");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseApi> getUserByUserId(@PathVariable String userId) {
        log.info("getUserByUserId start, userId: [{}]", userId);
        try {
            return EitherMapper.eitherMapper(userProfileService.getUserByUserId(userId));
        } finally {
            log.info("getUserByUserId end");
        }
    }

    @GetMapping
    public ResponseEntity<ResponseApi> getAllUsers() {
        log.info("getAllUsers start");
        try {
            return EitherMapper.eitherMapper(userProfileService.getAllUsers());
        } finally {
            log.info("getAllUsers end");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseApi> updateUser(
            @PathVariable String userId,
            @RequestBody @Validated UpdateUserRequestDTO request) {
        log.info("updateUser start, userId: [{}]", userId);
        try {
            return EitherMapper.eitherMapper(userProfileService.updateUser(userId, request));
        } finally {
            log.info("updateUser end");
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseApi> deleteUser(@PathVariable String userId) {
        log.info("deleteUser start, userId: [{}]", userId);
        try {
            return EitherMapper.eitherMapper(userProfileService.deleteUser(userId));
        } finally {
            log.info("deleteUser end");
        }
    }

    @PostMapping("/{userId}/roles")
    public ResponseEntity<ResponseApi> assignRole(
            @PathVariable String userId,
            @RequestBody @Validated AssignRoleRequestDTO request) {
        log.info("assignRole start, userId: [{}], roleCode: [{}]", userId, request.getRoleCode());
        try {
            return EitherMapper.eitherMapper(userProfileService.assignRole(userId, request));
        } finally {
            log.info("assignRole end");
        }
    }

    @DeleteMapping("/{userId}/roles/{roleCode}")
    public ResponseEntity<ResponseApi> removeRole(
            @PathVariable String userId,
            @PathVariable String roleCode) {
        log.info("removeRole start, userId: [{}], roleCode: [{}]", userId, roleCode);
        try {
            return EitherMapper.eitherMapper(userProfileService.removeRole(userId, roleCode));
        } finally {
            log.info("removeRole end");
        }
    }
}
