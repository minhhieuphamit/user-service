package com.acme.ecommerce.user.core.service;

import com.acme.ecommerce.user.core.businessexception.ErrorCode;
import com.acme.ecommerce.user.core.domain.request.AssignRoleRequestDTO;
import com.acme.ecommerce.user.core.domain.request.CreateUserRequestDTO;
import com.acme.ecommerce.user.core.domain.request.UpdateUserRequestDTO;
import com.acme.ecommerce.user.core.domain.response.UserProfileResponse;
import io.vavr.control.Either;

import java.util.List;

public interface UserProfileService {

    Either<ErrorCode, UserProfileResponse> createUser(CreateUserRequestDTO request);

    Either<ErrorCode, UserProfileResponse> getUserByUserId(String userId);

    Either<ErrorCode, List<UserProfileResponse>> getAllUsers();

    Either<ErrorCode, UserProfileResponse> updateUser(String userId, UpdateUserRequestDTO request);

    Either<ErrorCode, Void> deleteUser(String userId);

    Either<ErrorCode, UserProfileResponse> assignRole(String userId, AssignRoleRequestDTO request);

    Either<ErrorCode, UserProfileResponse> removeRole(String userId, String roleCode);
}
