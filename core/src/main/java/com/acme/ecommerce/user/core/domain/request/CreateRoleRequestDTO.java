package com.acme.ecommerce.user.core.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleRequestDTO {

    @NotBlank(message = "Role code cannot be blank")
    @Size(min = 1, max = 50, message = "Role code must be between 1 and 50 characters")
    private String roleCode;

    @NotBlank(message = "Role name cannot be blank")
    @Size(min = 1, max = 100, message = "Role name must be between 1 and 100 characters")
    private String roleName;
}
