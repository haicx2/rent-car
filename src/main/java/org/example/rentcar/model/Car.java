package org.example.rentcar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String licensePlate;
    private String brand;
    private String model;
    private String color;
    private int seats;
    private int productionYear;
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
    @Lob
    private Blob image;
    @ManyToOne
    private CarOwner owner;
}
