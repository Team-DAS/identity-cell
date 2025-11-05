package com.udeajobs.identity.auth_service.service;

import com.udeajobs.identity.auth_service.dto.AuthResponse;
import com.udeajobs.identity.auth_service.dto.LoginRequest;
import com.udeajobs.identity.auth_service.dto.NewAccessTokenResponse;
import com.udeajobs.identity.auth_service.dto.RefreshTokenRequest;
import com.udeajobs.identity.auth_service.entity.RefreshToken;
import com.udeajobs.identity.auth_service.entity.User;
import com.udeajobs.identity.auth_service.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de autenticación usando username y password.
 *
 * Gestiona la autenticación de usuarios mediante Spring Security, generación de tokens JWT
 * y renovación de access tokens mediante refresh tokens.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class UsernamePasswordAuthService implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    /**
     * Autentica un usuario con email y contraseña.
     *
     * Proceso de autenticación:
     * 1. Valida las credenciales con Spring Security
     * 2. Establece el contexto de seguridad
     * 3. Genera un access token JWT
     * 4. Crea un refresh token
     * 5. Retorna ambos tokens en la respuesta
     *
     * @param loginRequest credenciales del usuario (email y contraseña)
     * @return AuthResponse con access token, refresh token y tipo de token
     * @throws org.springframework.security.authentication.BadCredentialsException si las credenciales son incorrectas
     */
    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        // 1. Autenticar con Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        // 2. Guardar el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Extraer el UserDetails del objeto Authentication
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 4. Generar el token JWT usando el UserDetails
        String token = jwtProvider.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        // 5. Devolver la respuesta
        return new AuthResponse(token, refreshToken.getToken(), "Bearer" );
    }

    /**
     * Genera un nuevo access token usando un refresh token válido.
     *
     * Proceso de renovación:
     * 1. Busca el refresh token en la base de datos
     * 2. Verifica que no haya expirado
     * 3. Genera un nuevo access token JWT
     * 4. Retorna el nuevo access token con el mismo refresh token
     *
     * @param refreshTokenRequest objeto con el refresh token
     * @return NewAccessTokenResponse con el nuevo access token y el mismo refresh token
     * @throws RuntimeException si el refresh token no existe o ha expirado
     */
    @Override
    public NewAccessTokenResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {
        // 1. Buscar el refresh token en la base de datos
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenRequest.refreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        // 2. Verificar si el refresh token ha expirado
        refreshTokenService.verifyExpiration(refreshToken);

        // 3. Generar un nuevo token JWT
        User user = refreshToken.getUser();
        String newAccessToken = jwtProvider.generateToken(user);

        // 4. Devolver la respuesta
        return new NewAccessTokenResponse(newAccessToken, refreshToken.getToken());
    }
}
