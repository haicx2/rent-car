package org.example.rentcar.service.user;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.exception.AlreadyExistException;
import org.example.rentcar.exception.ResourceNotFoundException;
import org.example.rentcar.model.User;
import org.example.rentcar.repository.UserRepository;
import org.example.rentcar.request.RegisterRequest;
import org.example.rentcar.utils.FeedBackMessage;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User createUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AlreadyExistException("Email already exists");
        }
        User user = modelMapper.map(registerRequest, User.class);
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.FOUND));
    }
}
