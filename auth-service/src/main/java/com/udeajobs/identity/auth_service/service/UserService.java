package com.udeajobs.identity.auth_service.service;

import com.udeajobs.identity.auth_service.entity.User;
import com.udeajobs.identity.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio de gestión de usuarios que implementa UserDetailsService de Spring Security.
 *
 * Proporciona la integración con Spring Security para cargar usuarios desde la base de datos
 * durante el proceso de autenticación.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Carga un usuario por su email (username).
     *
     * Este método es utilizado por Spring Security durante el proceso de autenticación
     * para obtener los detalles del usuario desde la base de datos.
     *
     * @param username email del usuario (usado como username)
     * @return UserDetails con la información del usuario
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
