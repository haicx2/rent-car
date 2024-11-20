package org.example.rentcar.service.password;

import org.example.rentcar.request.ChangePasswordRequest;

public interface ChangePasswordService {
    void changePassword(long userId, ChangePasswordRequest passwordRequest);
}
