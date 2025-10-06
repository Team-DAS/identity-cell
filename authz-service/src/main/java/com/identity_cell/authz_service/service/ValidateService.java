package com.identity_cell.authz_service.service;

import com.identity_cell.authz_service.exception.InvalidTokenException;
import com.identity_cell.authz_service.exception.TokenIsNotPresentException;
import com.identity_cell.authz_service.util.JwtValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateService {

    private final JwtValidator jwtValidator;

    public boolean validateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new TokenIsNotPresentException("Token is not present");
        }
        String token = authHeader.substring(7);

        boolean isValid = jwtValidator.validateToken(token);

        if (isValid) {
            return true;
        } else {
            throw new InvalidTokenException("Invalid Token");
        }
    }
}
