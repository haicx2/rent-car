package org.example.rentcar.repository;

import org.example.rentcar.model.CarOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarOwnerRepository extends JpaRepository<CarOwner, Long> {
}
