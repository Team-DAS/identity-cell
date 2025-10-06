package com.udeajobs.identity.account_service.entity;

import com.udeajobs.identity.account_service.enums.ROLE;
import com.udeajobs.identity.account_service.enums.STATUS;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "user")
@Builder
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private ROLE role;
    private STATUS status;
    private String verificationCode;
    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenExpiration;
}
