package com.identity_cell.authz_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenIsNotPresentException extends RuntimeException {
    public TokenIsNotPresentException(String message) {
        super(message);
    }
}
