package org.example.rentcar.request;

import lombok.Data;

import java.time.Year;

@Data
public class UpdateCarRequest {
    private String name;
    private String licensePlate;
    private String brand;
    private String model;
    private String color;
    private int seats;
    private Year productionYear;
    private double basePrice;
    private String description;
}
