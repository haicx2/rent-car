package org.example.rentcar.service.booking;

import org.example.rentcar.model.Booking;
import org.example.rentcar.request.BookingRequest;
import org.example.rentcar.request.BookingUpdateRequest;

import java.util.List;

public interface BookingService {
    Booking bookCar(BookingRequest bookingRequest, long carId, long customerId);
    List<Booking> getAllBookings();
    Booking getBookingById(long bookingId);
    Booking getBookingByNo(String bookingNo);
    Booking updateBooking(BookingUpdateRequest bookingUpdateRequest, long bookingId);
    void deleteBooking(long bookingId);
}
