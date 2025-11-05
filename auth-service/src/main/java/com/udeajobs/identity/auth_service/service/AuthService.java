package com.udeajobs.identity.auth_service.service;

import com.udeajobs.identity.auth_service.dto.AuthResponse;
import com.udeajobs.identity.auth_service.dto.LoginRequest;
import com.udeajobs.identity.auth_service.dto.NewAccessTokenResponse;
import com.udeajobs.identity.auth_service.dto.RefreshTokenRequest;

/**
 * Interfaz de servicio para la gestión de autenticación de usuarios.
 *
 * Define los métodos principales para el login de usuarios y la renovación de tokens de acceso.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
public interface AuthService {
    /**
     * Autentica un usuario con sus credenciales.
     *
     * @param loginRequest objeto con email y contraseña del usuario
     * @return AuthResponse con access token, refresh token y tipo de token
     */
    AuthResponse login(LoginRequest loginRequest);

    /**
     * Renueva un access token usando un refresh token válido.
     *
     * @param refreshTokenRequest objeto con el refresh token
     * @return NewAccessTokenResponse con el nuevo access token y el mismo refresh token
     */
    NewAccessTokenResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest);
}
