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
import org.example.rentcar.repository.CarRepository;
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
    private final ModelMapper modelMapper;
    private final CarOwnerRepository carOwnerRepository;

    @Override
    public Booking bookCar(BookingRequest bookingRequest, long carId, long customerId) {
        Car car = carService.findById(carId);
        Booking bookingBefore = bookingRepository.findByStatusAndCarId(BookingStatus.APPROVED, carId);
        if(checkDateInRange(bookingBefore.getStartDate(), bookingRequest.getStartDate(), bookingRequest.getEndDate())
         || checkDateInRange(bookingBefore.getEndDate(), bookingRequest.getStartDate(), bookingRequest.getEndDate())) {
            throw new IllegalArgumentException("Car is already booked in that time");
        }
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_FOUND));
        Booking booking = modelMapper.map(bookingRequest, Booking.class);
        booking.setCar(car);
        booking.setCustomer(customer);
        booking.setBill(booking.getOverAllPrice());
        booking.setAppointmentNo();
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

    public void bookingStatusHandle(Booking booking) {
        if(booking.getStatus() == BookingStatus.COMPLETED) {
            Car car = carService.findById(booking.getCar().getId());
            Customer customer = customerRepository.findById(booking.getCustomer().getId()).orElseThrow(
                    () -> new ResourceNotFoundException(FeedBackMessage.NOT_FOUND)
            );
            CarOwner carOwner = carService.findCarOwnerByCarId(car.getId());
            customer.setWallet(customer.getWallet() - booking.getBill() +car.getDeposit());
            carOwner.setWallet(carOwner.getWallet() + booking.getBill());
            customerRepository.save(customer);
            carOwnerRepository.save(carOwner);
        }
        else if (booking.getStatus() == BookingStatus.REJECTED) {
            deleteBooking(booking.getId());
        } else if(booking.getStatus() == BookingStatus.APPROVED) {
            Car car = carService.findById(booking.getCar().getId());
            Customer customer = customerRepository.findById(booking.getCustomer().getId()).orElseThrow(
                    () -> new ResourceNotFoundException(FeedBackMessage.NOT_FOUND)
            );
            customer.setWallet(customer.getWallet() - car.getDeposit());
            customerRepository.save(customer);
        }
    }

    public boolean checkDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return (date.isAfter(startDate) || date.isEqual(startDate) || date.isBefore(endDate) || date.isEqual(endDate));
    }

    @Override
    public void deleteBooking(long bookingId) {
        Booking booking = getBookingById(bookingId);
        bookingRepository.delete(booking);
    }
}
