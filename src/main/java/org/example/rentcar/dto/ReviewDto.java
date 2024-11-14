package org.example.rentcar.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private String comment;
    private double stars;
    private CarDto carDto;
    private CustomerDto customerDto;
}
