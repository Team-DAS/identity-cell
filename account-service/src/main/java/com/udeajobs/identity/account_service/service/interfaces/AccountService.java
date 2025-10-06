package com.udeajobs.identity.account_service.service.interfaces;

import com.udeajobs.identity.account_service.entity.User;

public interface AccountService {
    User registerUser(User user);
    void verifyUser(String email, String verificationCode);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
}
