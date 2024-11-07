package org.example.rentcar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long nationalId;
    private String name;
    private LocalDate birthday;
    private String phone;
    private String email;
    private String password;
    private String address;
    private String drivingLicense;
    private double wallet;
    private String role;
}
