package com.udeajobs.identity.account_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.udeajobs.identity.account_service.entity.User;

import java.util.Optional;

/**
 * Repositorio para operaciones de acceso a datos de usuarios.
 *
 * Esta interfaz extiende MongoRepository para proporcionar operaciones CRUD
 * básicas y métodos de consulta personalizados para la entidad User.
 * Utiliza MongoDB como base de datos subyacente.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Busca un usuario por su dirección de correo electrónico.
     *
     * @param email la dirección de correo electrónico del usuario
     * @return Optional conteniendo el usuario si existe, Optional.empty() en caso contrario
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca un usuario por su token de recuperación de contraseña.
     *
     * @param token el token de recuperación de contraseña
     * @return Optional conteniendo el usuario si el token es válido, Optional.empty() en caso contrario
     */
    Optional<User> findByResetPasswordToken(String token);
}
