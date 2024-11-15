package org.example.rentcar.repository;

import org.example.rentcar.enums.BookingStatus;
import org.example.rentcar.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingNo(String bookingNo);

    Booking findByStatusAndCarId(BookingStatus bookingStatus, long carId);

    List<Booking> findAllByCustomerIdAndStatus(long customerId, BookingStatus bookingStatus);

    List<Booking> findAllByCarId(long carId);
}
