package com.udeajobs.identity.auth_service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Proveedor de tokens JWT para la generación y firma de access tokens.
 *
 * Utiliza la librería JJWT para crear tokens JWT con firma HMAC-SHA256.
 * Los tokens incluyen información del usuario (email) y sus roles.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@Component
@Data
@NoArgsConstructor
public class JwtProvider {

    /**
     * Clave secreta para firmar los tokens JWT, configurada en application.properties.
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Tiempo de expiración del token en milisegundos, configurado en application.properties.
     */
    @Value("${jwt.expiration}")
    private long expirationTime;

    /**
     * Genera un token JWT para un usuario autenticado.
     *
     * El token incluye:
     * - Subject: email del usuario
     * - Claims: rol del usuario
     * - Fecha de emisión
     * - Fecha de expiración
     * - Firma HMAC-SHA256
     *
     * @param userDetails detalles del usuario autenticado
     * @return token JWT firmado como String
     */
    public String generateToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Date currentDate = new Date();
        Date expiryDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .findFirst().orElse("USER"));

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Obtiene la clave de firma para los tokens JWT.
     *
     * Decodifica la clave secreta desde Base64 y crea una clave HMAC.
     *
     * @return clave HMAC para firmar tokens
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
