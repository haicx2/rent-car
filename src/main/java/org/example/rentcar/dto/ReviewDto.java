package org.example.rentcar.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private String comment;
    private double stars;
    private long customerId;
    private long carId;
    private String customerName;
    private String carName;
}
