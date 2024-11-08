package org.example.rentcar.request;

import lombok.Data;

import java.time.Year;

@Data
public class UpdateCarRequest {
    private String name;
    private String licensePlate;
    private String color;
    private int seats;
    private double mileage;
    private double fuelConsumption;
    private double basePrice;
    private double deposit;
    private String address;
    private String description;
    private String additionalFunction;
    private String termOfUse;
}
