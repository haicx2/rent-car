package org.example.rentcar.service.password;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.exception.ResourceNotFoundException;
import org.example.rentcar.model.User;
import org.example.rentcar.repository.UserRepository;
import org.example.rentcar.request.ChangePasswordRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChangePasswordServiceImpl implements ChangePasswordService {
    private final UserRepository userRepository;
    @Override
    public void changePassword(long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(Objects.equals(request.getCurrentPassword(), "")
                || Objects.equals(request.getNewPassword(), "")) {
            throw new IllegalArgumentException("All fields are required");
        }
        if(!Objects.equals(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password does not match");
        }
        if(!Objects.equals(request.getNewPassword(), request.getConfirmNewPassword())) {
            throw new IllegalArgumentException("Password confirmation mis-match ");
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }
}
