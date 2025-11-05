package com.udeajobs.identity.auth_service.controller;

import com.udeajobs.identity.auth_service.dto.LoginRequest;
import com.udeajobs.identity.auth_service.dto.NewAccessTokenResponse;
import com.udeajobs.identity.auth_service.dto.RefreshTokenRequest;
import com.udeajobs.identity.auth_service.dto.AuthResponse;
import com.udeajobs.identity.auth_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para la gestión de autenticación de usuarios.
 *
 * Proporciona endpoints para el login de usuarios y la renovación de tokens de acceso.
 * Todos los endpoints están bajo la ruta base {@code /api/v1/auth}.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API para autenticación de usuarios y gestión de tokens JWT")
public class AuthController {
    private final AuthService authService;

    /**
     * Autentica un usuario con email y contraseña.
     *
     * Valida las credenciales del usuario y genera un access token y un refresh token
     * si la autenticación es exitosa.
     *
     * @param loginRequest objeto con las credenciales del usuario (email y contraseña)
     * @return ResponseEntity con el access token, refresh token y tipo de token
     */
    @Operation(
            summary = "Login de usuario",
            description = "Autentica un usuario con email y contraseña. Retorna un access token JWT y un refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Credenciales inválidas o datos de entrada incorrectos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales incorrectas",
                    content = @Content
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }

    /**
     * Renueva un access token usando un refresh token válido.
     *
     * Valida el refresh token y genera un nuevo access token si el refresh token
     * es válido y no ha expirado.
     *
     * @param refreshTokenRequest objeto con el refresh token
     * @return ResponseEntity con el nuevo access token y el mismo refresh token
     */
    @Operation(
            summary = "Renovar access token",
            description = "Genera un nuevo access token usando un refresh token válido. El refresh token no se renueva."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token renovado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewAccessTokenResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Refresh token inválido o datos de entrada incorrectos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Refresh token expirado o no encontrado",
                    content = @Content
            )
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<NewAccessTokenResponse> refreshAccessToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return new ResponseEntity<>(authService.refreshAccessToken(refreshTokenRequest), HttpStatus.OK);
    }
}
