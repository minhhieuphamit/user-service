package com.acme.ecommerce.user.core.domain.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDTO {

    @NotBlank(message = "User ID cannot be blank")
    @Size(min = 1, max = 50, message = "User ID must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._@-]+$", message = "User ID can only contain letters, numbers, dots, underscores, @ and -")
    private String userId;

    @Email(message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    private String phoneNumber;
}
