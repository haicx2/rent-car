package org.example.rentcar.service.user;

import org.example.rentcar.model.User;
import org.example.rentcar.request.RegisterRequest;

public interface UserService {
    User createUser(RegisterRequest registerRequest);
    User getUserByEmail(String email);
}
