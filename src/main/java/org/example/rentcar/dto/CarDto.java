package org.example.rentcar.dto;

import lombok.Data;
import org.example.rentcar.model.CarOwner;

import java.time.Year;

@Data
public class CarDto {
    private long id;
    private String name;
    private String licensePlate;
    private String brand;
    private byte[] image;
    private String ownerEmail;
}
