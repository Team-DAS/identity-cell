package com.udeajobs.identity.account_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VerificationRequest(
        @NotNull(message = "Email is mandatory")
        String email,
        @NotNull(message = "Code is mandatory")
        @Pattern(regexp = "^[0-9a-zA-Z]{6}$", message = "Code must be 6 characters long")
        String code
) {
}
