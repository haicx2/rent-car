package org.example.rentcar.service.token;

import org.example.rentcar.model.User;
import org.example.rentcar.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {
    String validateToken(String token);

    void saveVerificationTokenForUser(String token, User user);

    VerificationToken generateNewVerificationToken(String oldToken);

    Optional<VerificationToken> findByToken(String token);

    void deleteVerificationToken(Long tokenId);

    boolean isTokenExpired(String token);
}
