package org.example.rentcar.request;

import lombok.Data;
import org.example.rentcar.model.User;

import java.util.Date;

@Data
public class VerificationTokenRequest {
    private String token;
    private Date expirationTime;
    private User user;
}
