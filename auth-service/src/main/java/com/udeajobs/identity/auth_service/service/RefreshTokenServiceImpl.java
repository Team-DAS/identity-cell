package com.udeajobs.identity.auth_service.service;

import com.udeajobs.identity.auth_service.entity.RefreshToken;
import com.udeajobs.identity.auth_service.repository.RefreshTokenRepository;
import com.udeajobs.identity.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

/**
 * Implementación del servicio de gestión de refresh tokens.
 *
 * Gestiona el ciclo de vida completo de los refresh tokens: creación, búsqueda,
 * verificación de expiración y eliminación de tokens vencidos.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    /**
     * Duración en milisegundos del refresh token, configurada en application.properties.
     */
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    /**
     * Busca un refresh token por su valor UUID.
     *
     * @param token valor UUID del refresh token
     * @return Optional con el refresh token si existe, Optional vacío si no se encuentra
     */
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Crea un nuevo refresh token para un usuario.
     *
     * Genera un UUID único, establece la fecha de expiración basada en la configuración
     * y asocia el token con el usuario especificado.
     *
     * @param userEmail email del usuario para quien se crea el refresh token
     * @return RefreshToken creado y guardado en la base de datos
     * @throws java.util.NoSuchElementException si el usuario no existe
     */
    @Override
    public RefreshToken createRefreshToken(String userEmail) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findByEmail(userEmail).orElseThrow());
        refreshToken.setExperyDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(java.util.UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Verifica si un refresh token ha expirado.
     *
     * Si el token ha expirado, lo elimina de la base de datos y lanza una excepción.
     * Si el token es válido, lo retorna sin modificaciones.
     *
     * @param token refresh token a verificar
     * @return el mismo refresh token si es válido
     * @throws RuntimeException si el token ha expirado
     */
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExperyDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
}
