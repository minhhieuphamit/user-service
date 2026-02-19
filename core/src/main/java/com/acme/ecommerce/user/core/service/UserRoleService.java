package com.acme.ecommerce.user.core.service;

import com.acme.ecommerce.user.core.businessexception.ErrorCode;
import com.acme.ecommerce.user.core.domain.request.CreateRoleRequestDTO;
import com.acme.ecommerce.user.core.domain.response.UserRoleResponse;
import io.vavr.control.Either;

import java.util.List;

public interface UserRoleService {

    Either<ErrorCode, UserRoleResponse> createRole(CreateRoleRequestDTO request);

    Either<ErrorCode, UserRoleResponse> getRoleByCode(String roleCode);

    Either<ErrorCode, List<UserRoleResponse>> getAllRoles();

    Either<ErrorCode, UserRoleResponse> updateRole(String roleCode, CreateRoleRequestDTO request);

    Either<ErrorCode, Void> deleteRole(String roleCode);

    Either<ErrorCode, Void> hardDeleteRole(String roleCode);
}
