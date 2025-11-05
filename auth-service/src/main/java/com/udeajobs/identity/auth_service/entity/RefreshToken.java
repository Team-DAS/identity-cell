package com.udeajobs.identity.auth_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Entidad que representa un refresh token en el sistema.
 *
 * Los refresh tokens se utilizan para generar nuevos access tokens sin necesidad
 * de volver a autenticarse. Se almacenan en la colección "refresh_tokens" de MongoDB.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Data
@Document(collection = "refresh_tokens")
public class RefreshToken {
    /**
     * Identificador único del refresh token.
     */
    @Id
    private String id;

    /**
     * Referencia al usuario propietario del refresh token.
     */
    @DBRef
    private User user;

    /**
     * Token UUID único para identificar el refresh token.
     */
    private String token;

    /**
     * Fecha y hora de expiración del refresh token.
     */
    private Instant experyDate;
}
