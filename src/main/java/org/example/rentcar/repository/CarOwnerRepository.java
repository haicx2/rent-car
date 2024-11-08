package org.example.rentcar.repository;

import org.example.rentcar.model.Car;
import org.example.rentcar.model.CarOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarOwnerRepository extends JpaRepository<CarOwner, Long> {

}
