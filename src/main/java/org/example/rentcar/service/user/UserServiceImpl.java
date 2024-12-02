package org.example.rentcar.service.user;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.config.Mapper;
import org.example.rentcar.dto.*;
import org.example.rentcar.exception.AlreadyExistException;
import org.example.rentcar.exception.ResourceNotFoundException;
import org.example.rentcar.model.Car;
import org.example.rentcar.model.CarOwner;
import org.example.rentcar.model.Customer;
import org.example.rentcar.model.User;
import org.example.rentcar.repository.*;
import org.example.rentcar.request.RegisterRequest;
import org.example.rentcar.request.UpdateUserRequest;
import org.example.rentcar.service.booking.BookingService;
import org.example.rentcar.service.car.CarService;
import org.example.rentcar.service.review.ReviewService;
import org.example.rentcar.service.role.RoleService;
import org.example.rentcar.utils.FeedBackMessage;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CarOwnerRepository carOwnerRepository;
    private final CustomerRepository customerRepository;
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;
    private final EntityConverter<User, UserDto> userEntityConverter;
    private final BookingService bookingService;
    private final CarRepository carRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    @Transactional
    public User createUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AlreadyExistException("Email already exists");
        }
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        switch (registerRequest.getRole()) {
            case "OWNER" ->{
                CarOwner user = modelMapper.map(registerRequest, CarOwner.class);
                user.setBirthday(LocalDate.parse(registerRequest.getBirthday()));
                user.setRoles(roleService.setUserRole("OWNER"));
                return carOwnerRepository.save(user);
            }
            case "CUSTOMER" ->{
                Customer user = modelMapper.map(registerRequest, Customer.class);
                user.setBirthday(LocalDate.parse(registerRequest.getBirthday()));
                user.setRoles(roleService.setUserRole("CUSTOMER"));
                return customerRepository.save(user);
            }
            default -> throw new ResourceNotFoundException("Invalid role");
        }

    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.FOUND));
    }

    @Override
    @Transactional
    public User updateUserById(long userId, UpdateUserRequest updateRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_FOUND));
        modelMapper.map(updateRequest, user);
        user.setBirthday(LocalDate.parse(updateRequest.getBirthday()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    @Override
    public User getUserById(long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_FOUND));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> userEntityConverter
                .mapEntityToDTO(user,UserDto.class))
                .toList();
    }

    @Override
    public UserDto getUserDetails(long userId) {
        User user = getUserById(userId);
        UserDto userDto = userEntityConverter.mapEntityToDTO(user,UserDto.class);
        if(user.getRole().equals("CUSTOMER")){
            userDto.setBookingDtos(bookingService.getBookingByCustomerId(user.getId()));
        }else {
            userDto.setBookingDtos(bookingService.getBookingByOwnerId(user.getId()));
        }
        userDto.setReviewDtos(reviewService.getReviewsByCustomerId(userId,0,5)
                .getContent()
                .stream()
                .map(mapper::mapReviewToDto)
                .collect(Collectors.toList())
        );
        if(user.getRole().equals("CUSTOMER")) {
            userDto.setCarDtos(null);
        } else {
            List<Car> cars = carRepository.findAllByOwnerId(user.getId());
            userDto.setCarDtos(cars.stream().map(car -> {
                CarDto carDto = new CarDto();
                carDto.setId(car.getId());
                carDto.setName(car.getName());
                carDto.setBrand(car.getBrand());
                carDto.setOwnerEmail(car.getOwner().getEmail());
                carDto.setLicensePlate(car.getLicensePlate());
                return carDto;
            }).collect(Collectors.toList()));
        }
        return userDto;
    }
}
