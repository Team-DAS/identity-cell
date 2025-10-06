package com.udeajobs.identity.account_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.udeajobs.identity.account_service.entity.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByResetPasswordToken(String token);
}
