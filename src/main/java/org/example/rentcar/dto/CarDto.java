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
    private String model;
    private String color;
    private int seats;
    private Year productionYear;
    private String transmissionType;
    private String fuelType;
    private double mileage;
    private double fuelConsumption;
    private double basePrice;
    private double deposit;
    private String address;
    private String description;
    private String additionalFunction;
    private String termOfUse;
    private byte[] image;
    private String ownerEmail;
}
