package org.example.rentcar.controller;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.model.Booking;
import org.example.rentcar.request.BookingRequest;
import org.example.rentcar.request.BookingUpdateRequest;
import org.example.rentcar.response.APIResponse;
import org.example.rentcar.service.booking.BookingService;
import org.example.rentcar.utils.FeedBackMessage;
import org.example.rentcar.utils.UrlMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:5173/")
@RestController
@RequestMapping(UrlMapping.BOOKING)
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    @PostMapping(UrlMapping.ADD)
    public ResponseEntity<APIResponse> bookCar(@RequestBody BookingRequest bookingRequest,
                                               @RequestParam long carId,
                                               @RequestParam long customerId) {
        bookingService.bookCar(bookingRequest, carId, customerId);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, bookingRequest));
    }

    @GetMapping(UrlMapping.GET_ALL)
    public ResponseEntity<APIResponse> getBookings() {
        List<Booking> bookingList = bookingService.getAllBookings();
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, bookingList));
    }

    @GetMapping(UrlMapping.GET_BY_ID)
    public ResponseEntity<APIResponse> getBooking(@PathVariable long id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, booking));
    }

    @PutMapping(UrlMapping.UPDATE_BY_ID)
    public ResponseEntity<APIResponse> updateBooking(@PathVariable long id,
                                                     @RequestBody BookingUpdateRequest bookingUpdateRequest) {
        Booking booking = bookingService.updateBooking(bookingUpdateRequest,id);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, booking));
    }

    @DeleteMapping(UrlMapping.DELETE_BY_ID)
    public ResponseEntity<APIResponse> deleteBooking(@PathVariable long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.DELETE_SUCCESS,null));
    }

}
