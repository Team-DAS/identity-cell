package com.udeajobs.identity.auth_service.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}
