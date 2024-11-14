package org.example.rentcar.service.user;

import org.example.rentcar.dto.UserDto;
import org.example.rentcar.model.User;
import org.example.rentcar.request.RegisterRequest;
import org.example.rentcar.request.UpdateUserRequest;

import java.util.List;

public interface UserService {
    User createUser(RegisterRequest registerRequest);
    User getUserByEmail(String email);
    User updateUserById(long userId, UpdateUserRequest updateRequest);
    void deleteUserById(long userId);
    User getUserById(long userId);
    List<UserDto> getAllUsers();
    UserDto getUserDetails(long userId);
}
