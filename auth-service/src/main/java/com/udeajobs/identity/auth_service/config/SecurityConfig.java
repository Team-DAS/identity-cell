package com.udeajobs.identity.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad de Spring Security para el servicio de autenticación.
 *
 * Define la configuración de autenticación, autorización y encriptación de contraseñas.
 * Configura los endpoints públicos y protegidos de la aplicación.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Configura el codificador de contraseñas usando BCrypt.
     *
     * BCrypt es un algoritmo de hash adaptativo que incluye sal automáticamente
     * y es resistente a ataques de fuerza bruta.
     *
     * @return instancia de BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el AuthenticationManager de Spring Security.
     *
     * El AuthenticationManager es responsable de autenticar las credenciales
     * del usuario durante el proceso de login.
     *
     * @param configuration configuración de autenticación de Spring Security
     * @return instancia de AuthenticationManager
     * @throws Exception si hay un error en la configuración
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     *
     * Define:
     * - Deshabilitación de CSRF (no necesario para APIs REST con tokens)
     * - Endpoints públicos: autenticación, OAuth2, actuator y Swagger
     * - Todos los demás endpoints requieren autenticación
     *
     * @param http objeto HttpSecurity para configurar la seguridad
     * @return cadena de filtros de seguridad configurada
     * @throws Exception si hay un error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/v1/auth/**",
                        "/login/oauth2/**",
                        "/actuator/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}