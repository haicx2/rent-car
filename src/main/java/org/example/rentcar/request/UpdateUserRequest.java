package org.example.rentcar.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    private Long nationalId;
    private String name;
    private String phone;
    private String address;
    private String drivingLicense;
}
