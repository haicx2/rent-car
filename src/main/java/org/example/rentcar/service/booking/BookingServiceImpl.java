package org.example.rentcar.service.booking;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.enums.BookingStatus;
import org.example.rentcar.exception.ResourceNotFoundException;
import org.example.rentcar.model.Booking;
import org.example.rentcar.model.Car;
import org.example.rentcar.model.CarOwner;
import org.example.rentcar.model.Customer;
import org.example.rentcar.repository.BookingRepository;
import org.example.rentcar.repository.CarOwnerRepository;
import org.example.rentcar.repository.CustomerRepository;
import org.example.rentcar.request.BookingRequest;
import org.example.rentcar.request.BookingUpdateRequest;
import org.example.rentcar.service.car.CarService;
import org.example.rentcar.utils.FeedBackMessage;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final CarService carService;
    private final CustomerRepository customerRepository;
    private final CarOwnerRepository carOwnerRepository;

    @Override
    @Transactional
    public Booking bookCar(BookingRequest bookingRequest, long carId, long customerId) {
        Car car = carService.findById(carId);
        Booking bookingBefore = bookingRepository.findByStatusAndCarId(BookingStatus.APPROVED, carId);
        if (bookingBefore != null && (checkDateInRange(bookingBefore.getStartDate(), LocalDate.parse(bookingRequest.getStartDate()), LocalDate.parse(bookingRequest.getEndDate()))
                || checkDateInRange(bookingBefore.getEndDate(), LocalDate.parse(bookingRequest.getStartDate()), LocalDate.parse(bookingRequest.getEndDate())))) {
            throw new IllegalArgumentException("Car is already booked in that time");
        }
        Customer customer = getCustomerById(customerId);
        Booking booking = new Booking();
        booking.setPaymentMethod(bookingRequest.getPaymentMethod());
        booking.setStartDate(LocalDate.parse(bookingRequest.getStartDate()));
        booking.setEndDate(LocalDate.parse(bookingRequest.getEndDate()));
        booking.setCar(car);
        booking.setCustomer(customer);
        booking.setAppointmentNo();
        booking.setBill(booking.getOverAllPrice());
        booking.setStatus(BookingStatus.WAITING_FOR_APPROVAL);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_FOUND));
    }

    @Override
    public Booking getBookingByNo(String bookingNo) {
        return bookingRepository.findByBookingNo(bookingNo).orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_FOUND));
    }

    @Override
    @Transactional
    public Booking updateBooking(BookingUpdateRequest bookingUpdateRequest, long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(bookingUpdateRequest.getStatus());
        bookingStatusHandle(booking);
        return bookingRepository.save(booking);
    }

    @Transactional
    public void bookingStatusHandle(Booking booking) {
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            Car car = carService.findById(booking.getCar().getId());
            Customer customer = getCustomerById(booking.getCustomer().getId());
            CarOwner carOwner = carService.findCarOwnerByCarId(car.getId());
            customer.setWallet(customer.getWallet() - booking.getBill() + car.getDeposit());
            carOwner.setWallet(carOwner.getWallet() + booking.getBill());
            customerRepository.save(customer);
            carOwnerRepository.save(carOwner);
        } else if (booking.getStatus() == BookingStatus.REJECTED) {
            deleteBooking(booking.getId());
        } else if (booking.getStatus() == BookingStatus.APPROVED) {
            Car car = carService.findById(booking.getCar().getId());
            Customer customer = getCustomerById(booking.getCustomer().getId());
            customer.setWallet(customer.getWallet() - car.getDeposit());
            customerRepository.save(customer);
        }
    }

    public boolean checkDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    private Customer getCustomerById(long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_FOUND));
    }

    @Override
    public void deleteBooking(long bookingId) {
        Booking booking = getBookingById(bookingId);
        bookingRepository.delete(booking);
    }
}
