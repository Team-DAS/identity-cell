package com.udeajobs.identity.account_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para el servicio de cuentas.
 *
 * Esta clase configura Spring Security para el microservicio de cuentas,
 * estableciendo las políticas de autenticación, autorización y encriptación
 * de contraseñas necesarias para el manejo seguro de cuentas de usuario.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura el encoder de contraseñas utilizando BCrypt.
     *
     * BCrypt es un algoritmo de hash adaptativo que incluye salt automático
     * y es resistente a ataques de fuerza bruta debido a su factor de costo configurable.
     *
     * @return una instancia de BCryptPasswordEncoder para encriptar contraseñas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad para las peticiones HTTP.
     *
     * Actualmente permite todas las peticiones sin autenticación para facilitar
     * el desarrollo inicial. En producción, se deben implementar controles
     * de acceso más restrictivos.
     *
     * @param http el objeto HttpSecurity para configurar la seguridad
     * @return la cadena de filtros de seguridad configurada
     * @throws Exception si ocurre un error durante la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        return http.build();
    }

}
