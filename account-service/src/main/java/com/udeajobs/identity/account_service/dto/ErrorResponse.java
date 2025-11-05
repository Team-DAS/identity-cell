package com.udeajobs.identity.account_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO para respuestas de error estandarizadas en la API.
 *
 * Esta clase proporciona una estructura consistente para todas las respuestas
 * de error del servicio, incluyendo código de estado HTTP, mensaje descriptivo,
 * tipo de error y timestamp de ocurrencia.
 *
 * @param status código de estado HTTP del error
 * @param message mensaje descriptivo del error (puede ser String o lista de errores)
 * @param error tipo o categoría del error
 * @param timestamp momento exacto en que ocurrió el error
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Schema(description = "Respuesta estándar de error de la API")
public record ErrorResponse(
        @Schema(
                description = "Código de estado HTTP del error",
                example = "400"
        )
        int status,

        @Schema(
                description = "Mensaje descriptivo del error o lista de errores de validación",
                example = "Invalid email format"
        )
        Object message,

        @Schema(
                description = "Tipo o categoría del error",
                example = "Bad Request"
        )
        String error,

        @Schema(
                description = "Timestamp de cuando ocurrió el error",
                example = "2025-11-05T14:30:00"
        )
        LocalDateTime timestamp
) {
    /**
     * Constructor de conveniencia que establece automáticamente el timestamp actual.
     *
     * @param status código de estado HTTP del error
     * @param message mensaje descriptivo del error
     * @param error tipo o categoría del error
     */
    public ErrorResponse(int status, Object message, String error) {
        this(status, message, error, LocalDateTime.now());
    }
}
