package org.example.rentcar.service.password;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.event.PasswordResetEvent;
import org.example.rentcar.exception.ResourceNotFoundException;
import org.example.rentcar.model.User;
import org.example.rentcar.model.VerificationToken;
import org.example.rentcar.repository.UserRepository;
import org.example.rentcar.repository.VerificationTokenRepository;
import org.example.rentcar.utils.FeedBackMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public Optional<User> findUserByPasswordResetToken(String token) {
        return tokenRepository.findByToken(token).map(VerificationToken::getUser);
    }

    @Override
    public void requestPasswordReset(String email) {
        userRepository.findByEmail(email).ifPresentOrElse(user -> {
            PasswordResetEvent passwordResetEvent = new PasswordResetEvent(this, user);
            eventPublisher.publishEvent(passwordResetEvent);
        }, () -> {throw new ResourceNotFoundException(FeedBackMessage.NO_USER_FOUND+email);});

    }

    @Override
    public String resetPassword(String password, User user) {
        try{
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return FeedBackMessage.PASSWORD_RESET_SUCCESS;
        }   catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
