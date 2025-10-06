package com.udeajobs.identity.auth_service.dto;

public record NewAccessTokenResponse(
        String accessToken,
        String refreshToken
) {
}
