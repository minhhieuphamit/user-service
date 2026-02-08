package com.acme.ecommerce.user.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profile_roles")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_profile_id", nullable = false)
    private Long userProfileId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "role_code")
    private String roleCode;
}
