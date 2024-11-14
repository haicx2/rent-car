package org.example.rentcar.repository;

import org.example.rentcar.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review  r where r.car.id =:carId")
    Page<Review> findAllByCarId(@Param("carId") long carId, Pageable pageable);
    @Query("select r from Review  r where r.customer.id =:customerId")
    Page<Review> findAllByCustomerId(@Param("customerId") long customerId, Pageable pageable);
}