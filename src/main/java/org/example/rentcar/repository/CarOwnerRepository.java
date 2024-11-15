package org.example.rentcar.repository;

import org.example.rentcar.model.Car;
import org.example.rentcar.model.CarOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarOwnerRepository extends JpaRepository<CarOwner, Long> {
    @Query("SELECT co FROM CarOwner co JOIN co.cars c WHERE c.id = :carId")
    CarOwner findByCarId(@Param("carId") long carId);
}
