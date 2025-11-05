package com.udeajobs.identity.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para la solicitud de renovación de access token.
 *
 * Contiene el refresh token necesario para generar un nuevo access token.
 *
 * @param refreshToken token de actualización válido
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Schema(description = "Solicitud de renovación de access token")
public record RefreshTokenRequest(
        @Schema(description = "Refresh token válido", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
        String refreshToken
) {
}
