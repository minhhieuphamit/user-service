package com.acme.ecommerce.user.core.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignRoleRequestDTO {

    @NotBlank(message = "Role code cannot be blank")
    private String roleCode;
}
