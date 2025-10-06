package com.udeajobs.identity.account_service.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        Object message,
        String error,
        LocalDateTime timestamp
) {
    public ErrorResponse(int status, Object message, String error) {
        this(status, message, error, LocalDateTime.now());
    }
}
