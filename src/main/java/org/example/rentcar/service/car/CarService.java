package org.example.rentcar.service.car;

import org.example.rentcar.dto.BookingDto;
import org.example.rentcar.dto.CarDto;
import org.example.rentcar.dto.ReviewDto;
import org.example.rentcar.model.Car;
import org.example.rentcar.model.CarOwner;
import org.example.rentcar.request.CarRegisterRequest;
import org.example.rentcar.request.UpdateCarRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CarService {
    Car save(CarRegisterRequest carRegisterRequest, long ownerId) throws IOException;
    Page<Car> findAll(int size, int page);
    Car findById(long carId);
    List<CarDto> findByOwnerId(long ownerId);
    Car update(long carId, UpdateCarRequest carRequest);
    void deleteById(long carId);
    Car saveImage(long carId, MultipartFile file) throws IOException;
    CarOwner findCarOwnerByCarId(long carId);
    List<CarDto> findAllCarsNoPage();
    List<CarDto> findCarBySearchQuery(String ownerEmail, String carName, String brand);
    CarDto getCarDetails(long carId);
}
