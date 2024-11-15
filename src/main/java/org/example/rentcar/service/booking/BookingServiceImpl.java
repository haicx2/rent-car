package org.example.rentcar.service.booking;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.config.Mapper;
import org.example.rentcar.dto.BookingDto;
import org.example.rentcar.enums.BookingStatus;
import org.example.rentcar.exception.ResourceNotFoundException;
import org.example.rentcar.model.Booking;
import org.example.rentcar.model.Car;
import org.example.rentcar.model.CarOwner;
import org.example.rentcar.model.Customer;
import org.example.rentcar.repository.BookingRepository;
import org.example.rentcar.repository.CarOwnerRepository;
import org.example.rentcar.repository.CarRepository;
import org.example.rentcar.repository.CustomerRepository;
import org.example.rentcar.request.BookingRequest;
import org.example.rentcar.request.BookingUpdateRequest;
import org.example.rentcar.utils.FeedBackMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final CarOwnerRepository carOwnerRepository;
    private final Mapper mapper;

    @Override
    @Transactional
    public Booking bookCar(BookingRequest bookingRequest, long carId, long customerId) {
        Car car = getCarById(carId);
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
    public BookingDto getBookingDtoById(long bookingId) {
        Booking booking = getBookingById(bookingId);
        return mapper.mapBookingToDto(booking);
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
            Car car = getCarById(booking.getCar().getId());
            Customer customer = getCustomerById(booking.getCustomer().getId());
            CarOwner carOwner = carOwnerRepository.findByCarId(booking.getCar().getId());
            customer.setWallet(customer.getWallet() - booking.getBill() + car.getDeposit());
            carOwner.setWallet(carOwner.getWallet() + booking.getBill());
            customerRepository.save(customer);
            carOwnerRepository.save(carOwner);
        } else if (booking.getStatus() == BookingStatus.REJECTED) {
            deleteBooking(booking.getId());
        } else if (booking.getStatus() == BookingStatus.APPROVED) {
            Car car = getCarById(booking.getCar().getId());
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


    @Override
    public List<BookingDto> getBookingByCustomerId(long customerId) {
        List<Booking> bookings = bookingRepository.findAllByCustomerIdAndStatus(customerId, BookingStatus.APPROVED);
        if (bookings.isEmpty()) {
            return null;
        }
        return bookings.stream().map(mapper::mapBookingToDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingByCarId(long carId) {
        List<Booking> bookings = bookingRepository.findAllByCarId(carId);
        if (bookings.isEmpty()) {
            return null;
        }
        return bookings.stream().map(mapper::mapBookingToDto).collect(Collectors.toList());
    }

    public Car getCarById(long carId) {
        return carRepository.findById(carId).orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_FOUND));
    }

}
