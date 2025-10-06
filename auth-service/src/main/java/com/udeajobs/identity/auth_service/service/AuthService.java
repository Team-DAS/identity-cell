package com.udeajobs.identity.auth_service.service;

import com.udeajobs.identity.auth_service.dto.AuthResponse;
import com.udeajobs.identity.auth_service.dto.LoginRequest;
import com.udeajobs.identity.auth_service.dto.NewAccessTokenResponse;
import com.udeajobs.identity.auth_service.dto.RefreshTokenRequest;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    NewAccessTokenResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest);
}
