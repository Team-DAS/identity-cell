package com.udeajobs.identity.account_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ROLE {
    FREELANCER,
    EMPLOYER;

    @JsonCreator
    public static ROLE fromString(String value) {
        try {
            return ROLE.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + value);
        }
    }
}
