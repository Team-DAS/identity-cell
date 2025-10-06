package com.udeajobs.identity.account_service.service.implementation;

import com.udeajobs.identity.account_service.entity.User;
import com.udeajobs.identity.account_service.enums.STATUS;
import com.udeajobs.identity.account_service.repository.UserRepository;
import com.udeajobs.identity.account_service.service.interfaces.AccountService;
import com.udeajobs.identity.account_service.service.interfaces.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Value("${app.reset-url-base}")
    private String resetUrlBase;

    @Override
    public User registerUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(STATUS.PENDING_VERIFICATION);
        user.setVerificationCode(String.format("%06d", new Random().nextInt(999999)));

        User savedUser = userRepository.save(user);

        Map<String, Object> model = new HashMap<>();
        model.put("username", savedUser.getUsername());
        model.put("verificationCode", savedUser.getVerificationCode());
        mailService.sendEmail(savedUser.getEmail(), "Verificación de cuenta - UdeAJobs", "verification-email", model);

        return savedUser;
    }

    @Override
    public void verifyUser(String email, String verificationCode) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        if (!user.get().getVerificationCode().equals(verificationCode)) {
            throw new IllegalArgumentException("Invalid verification code");
        }
        user.get().setStatus(STATUS.ACTIVE);
        user.get().setVerificationCode(null); // Clear verification code after use
        userRepository.save(user.get());
    }

    @Override
    public void forgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiration(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour
        userRepository.save(user);

        String resetUrl = resetUrlBase + "?token=" + token;

        Map<String, Object> model = new HashMap<>();
        model.put("username", user.getUsername());
        model.put("resetUrl", resetUrl);
        mailService.sendEmail(user.getEmail(), "Recuperación de Contraseña - UdeAJobs", "password-reset-link", model);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetPasswordToken(token);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }
        User user = userOptional.get();

        if (user.getResetPasswordTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiration(null);
        userRepository.save(user);
    }
}
