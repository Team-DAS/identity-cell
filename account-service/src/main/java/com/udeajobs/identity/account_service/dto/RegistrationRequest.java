package com.udeajobs.identity.account_service.dto;

import com.udeajobs.identity.account_service.enums.ROLE;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegistrationRequest(
    @NotBlank(message = "Username is mandatory") String username,
    @NotBlank(message = "Password is mandatory")
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message =
                "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase"
                    + " letter, one number and one special character")
        String password,
    @NotBlank(message = "Email is mandatory") @Email(message = "Email should be valid") String email,
    @NotNull(message = "Role is mandatory") ROLE role) {}
