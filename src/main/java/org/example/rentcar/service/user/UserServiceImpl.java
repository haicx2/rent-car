package org.example.rentcar.service.user;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.dto.EntityConverter;
import org.example.rentcar.dto.UserDto;
import org.example.rentcar.exception.AlreadyExistException;
import org.example.rentcar.exception.ResourceNotFoundException;
import org.example.rentcar.model.CarOwner;
import org.example.rentcar.model.Customer;
import org.example.rentcar.model.User;
import org.example.rentcar.repository.CarOwnerRepository;
import org.example.rentcar.repository.CustomerRepository;
import org.example.rentcar.repository.UserRepository;
import org.example.rentcar.request.RegisterRequest;
import org.example.rentcar.request.UpdateUserRequest;
import org.example.rentcar.utils.FeedBackMessage;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CarOwnerRepository carOwnerRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final EntityConverter<User, UserDto> userEntityConverter;

    @Override
    @Transactional
    public User createUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AlreadyExistException("Email already exists");
        }

        switch (registerRequest.getRole()) {
            case "OWNER" ->{
                CarOwner user = modelMapper.map(registerRequest, CarOwner.class);
                return carOwnerRepository.save(user);
            }
            case "CUSTOMER" ->{
                Customer user = modelMapper.map(registerRequest, Customer.class);
                return customerRepository.save(user);
            }
            default -> throw new ResourceNotFoundException("Invalid role");
        }

    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.FOUND));
    }

    @Override
    @Transactional
    public User updateUserById(long userId, UpdateUserRequest updateRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_FOUND));
        modelMapper.map(updateRequest, user);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_FOUND));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> userEntityConverter.mapEntityToDTO(user,UserDto.class)).toList();
    }
}
