package com.acme.ecommerce.user.core.domain.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

    private Long id;
    private String userId;
    private Boolean status;
    private String email;
    private String phoneNumber;
    private List<String> roles;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
