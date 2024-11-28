package org.example.rentcar.service.password;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.event.PasswordResetEvent;
import org.example.rentcar.exception.ResourceNotFoundException;
import org.example.rentcar.model.User;
import org.example.rentcar.model.VerificationToken;
import org.example.rentcar.repository.UserRepository;
import org.example.rentcar.repository.VerificationTokenRepository;
import org.example.rentcar.request.ChangePasswordRequest;
import org.example.rentcar.utils.FeedBackMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChangePasswordServiceImpl implements ChangePasswordService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(Objects.equals(request.getCurrentPassword(), "")
                || Objects.equals(request.getNewPassword(), "")) {
            throw new IllegalArgumentException("All fields are required");
        }
        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password does not match");
        }
        if(!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new IllegalArgumentException("Password confirmation mis-match ");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
