package com.udeajobs.identity.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para la respuesta de autenticación exitosa.
 *
 * Contiene los tokens generados tras un login exitoso: access token, refresh token y tipo de token.
 *
 * @param accessToken token JWT de acceso de corta duración
 * @param refreshToken token de actualización de larga duración
 * @param tokenType tipo de token (generalmente "Bearer")
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Schema(description = "Respuesta de autenticación con tokens JWT")
public record AuthResponse(
        @Schema(description = "Token JWT de acceso", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,
        @Schema(description = "Token de actualización", example = "550e8400-e29b-41d4-a716-446655440000")
        String refreshToken,
        @Schema(description = "Tipo de token", example = "Bearer")
        String tokenType
) {
}
