package com.udeajobs.identity.auth_service.repository;

import com.udeajobs.identity.auth_service.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repositorio para la gestión de usuarios en MongoDB.
 *
 * Proporciona operaciones CRUD básicas y consultas personalizadas para la entidad User.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
public interface UserRepository extends MongoRepository<User, String> {
    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email correo electrónico del usuario
     * @return Optional con el usuario si se encuentra, Optional vacío si no existe
     */
    Optional<User> findByEmail(String email);
}
