package com.udeajobs.identity.auth_service.service;

import com.udeajobs.identity.auth_service.entity.RefreshToken;

import java.util.Optional;

/**
 * Interfaz de servicio para la gestión de refresh tokens.
 *
 * Define los métodos para crear, buscar y verificar la validez de refresh tokens.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
public interface RefreshTokenService {
    /**
     * Busca un refresh token por su valor.
     *
     * @param token valor UUID del refresh token
     * @return Optional con el refresh token si existe, Optional vacío si no se encuentra
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Crea un nuevo refresh token para un usuario.
     *
     * @param userEmail email del usuario para quien se crea el refresh token
     * @return RefreshToken creado y guardado en la base de datos
     */
    RefreshToken createRefreshToken(String userEmail);

    /**
     * Verifica si un refresh token ha expirado.
     *
     * Si el token ha expirado, lo elimina de la base de datos y lanza una excepción.
     *
     * @param token refresh token a verificar
     * @return el mismo refresh token si es válido
     * @throws RuntimeException si el token ha expirado
     */
    RefreshToken verifyExpiration(RefreshToken token);
}
