package org.example.rentcar.repository;

import org.example.rentcar.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Query("select c from Car c where c.owner.id =:ownerId")
    List<Car> findAllByOwnerId(@Param("ownerId") long ownerId);
}
