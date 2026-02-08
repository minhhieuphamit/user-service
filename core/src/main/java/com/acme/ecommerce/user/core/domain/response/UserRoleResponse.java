package com.acme.ecommerce.user.core.domain.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleResponse {

    private Long id;
    private String roleCode;
    private String roleName;
    private Boolean status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
