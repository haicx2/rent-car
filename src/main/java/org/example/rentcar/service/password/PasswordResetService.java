package org.example.rentcar.service.password;

import org.example.rentcar.model.User;

import java.util.Optional;

public interface PasswordResetService {
    Optional<User> findUserByPasswordResetToken(String token);
    void requestPasswordReset(String email);
    String resetPassword(String password, User user);
}
