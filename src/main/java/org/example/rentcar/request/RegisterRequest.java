package org.example.rentcar.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private Long nationalId;
    private String name;
    private String birthday;
    private String phone;
    private String email;
    private String password;
    private String address;
    private String drivingLicense;
    private double wallet;
    private String role;
}
