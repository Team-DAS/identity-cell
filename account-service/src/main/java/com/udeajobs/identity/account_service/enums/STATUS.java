package com.udeajobs.identity.account_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum STATUS {
    PENDING_VERIFICATION,
    ACTIVE,
    INACTIVE;

    @JsonCreator
    public static STATUS fromString(String value) {
        try {
            return STATUS.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + value);
        }
    }
}
