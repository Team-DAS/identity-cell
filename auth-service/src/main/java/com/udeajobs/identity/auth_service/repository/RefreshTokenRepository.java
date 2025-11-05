package com.udeajobs.identity.auth_service.repository;

import com.udeajobs.identity.auth_service.entity.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repositorio para la gestión de refresh tokens en MongoDB.
 *
 * Proporciona operaciones CRUD básicas y consultas personalizadas para la entidad RefreshToken.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    /**
     * Busca un refresh token por su valor de token.
     *
     * @param token valor UUID del refresh token
     * @return Optional con el refresh token si se encuentra, Optional vacío si no existe
     */
    Optional<RefreshToken> findByToken(String token);
}
