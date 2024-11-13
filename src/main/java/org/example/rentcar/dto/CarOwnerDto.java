package org.example.rentcar.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CarOwnerDto {
    private long id;
    private Long nationalId;
    private String name;
    private LocalDate birthday;
    private String phone;
    private String email;
    private String address;
    private String drivingLicense;
    private double wallet;
    private String role;
    private List<BookingDto> bookingDtos;
    private List<ReviewDto> reviewDtos;
    private List<CarDto> carDtos;
}
