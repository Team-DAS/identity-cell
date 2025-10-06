package com.udeajobs.identity.account_service.controller;


import com.udeajobs.identity.account_service.dto.RegistrationRequest;
import com.udeajobs.identity.account_service.dto.ResetPasswordRequest;
import com.udeajobs.identity.account_service.dto.VerificationRequest;
import com.udeajobs.identity.account_service.dto.ForgotPasswordRequest;
import com.udeajobs.identity.account_service.entity.User;
import com.udeajobs.identity.account_service.service.interfaces.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<User> registerAccount(@Valid @RequestBody RegistrationRequest registrationRequest) {
        User user = User.builder()
                .username(registrationRequest.username())
                .email(registrationRequest.email())
                .password(registrationRequest.password())
                .role(registrationRequest.role())
                .build();
        return new ResponseEntity<>(accountService.registerUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyAccount(@Valid @RequestBody VerificationRequest verificationRequest) {
        accountService.verifyUser(verificationRequest.email(), verificationRequest.code());
        return new ResponseEntity<>("User verified successfully",HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        accountService.forgotPassword(forgotPasswordRequest.email());
        return new ResponseEntity<>("Password reset code sent to your email", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        accountService.resetPassword(resetPasswordRequest.token(), resetPasswordRequest.newPassword());
        return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
    }
}
