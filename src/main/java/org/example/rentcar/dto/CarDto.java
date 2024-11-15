package org.example.rentcar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.rentcar.model.CarOwner;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private long id;
    private String name;
    private String licensePlate;
    private String brand;
    private byte[] image;
    private String ownerEmail;
    private double averageRating;
    private List<ReviewDto> reviews = new ArrayList<>();
    private Long totalCustomers;
    private List<BookingDto> bookings = new ArrayList<>();
}
