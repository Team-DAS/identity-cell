package com.udeajobs.identity.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para la respuesta de renovación de access token.
 *
 * Contiene el nuevo access token generado y el mismo refresh token original.
 *
 * @param accessToken nuevo token JWT de acceso
 * @param refreshToken mismo refresh token original
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Schema(description = "Respuesta de renovación de access token")
public record NewAccessTokenResponse(
        @Schema(description = "Nuevo token JWT de acceso", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,
        @Schema(description = "Mismo refresh token original", example = "550e8400-e29b-41d4-a716-446655440000")
        String refreshToken
) {
}
