package com.udeajobs.identity.auth_service.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Entidad que representa un usuario en el sistema.
 *
 * Esta clase implementa {@link UserDetails} de Spring Security para integrarse
 * con el sistema de autenticación. Se almacena en la colección "user" de MongoDB.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Data
@Document(collection = "user")
@NoArgsConstructor
public class User implements UserDetails {
    /**
     * Identificador único del usuario.
     */
    @Id
    private String id;

    /**
     * Nombre de usuario.
     */
    private String username;

    /**
     * Contraseña encriptada del usuario.
     */
    private String password;

    /**
     * Correo electrónico del usuario (usado como username para autenticación).
     */
    private String email;

    /**
     * Rol del usuario en el sistema (ej: ROLE_USER, ROLE_ADMIN).
     */
    private String role;

    /**
     * Retorna las autoridades (roles) del usuario para Spring Security.
     *
     * @return colección de autoridades del usuario
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == null) {
            return Collections.emptyList();
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.role);
        return Collections.singletonList(authority);
    }

    /**
     * Retorna el username usado para autenticación (en este caso, el email).
     *
     * @return email del usuario
     */
    @Override
    public String getUsername() {
        return this.email;
    }
}
