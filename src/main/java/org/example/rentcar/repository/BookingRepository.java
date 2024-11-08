package org.example.rentcar.repository;

import org.example.rentcar.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingNo(String bookingNo);
}
