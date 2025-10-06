package com.udeajobs.identity.auth_service.service;

import com.udeajobs.identity.auth_service.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken createRefreshToken(String userEmail);
    RefreshToken verifyExpiration(RefreshToken token);
}
