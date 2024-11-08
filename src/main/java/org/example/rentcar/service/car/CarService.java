package org.example.rentcar.service.car;

import org.example.rentcar.model.Car;
import org.example.rentcar.model.CarOwner;
import org.example.rentcar.request.CarRegisterRequest;
import org.example.rentcar.request.UpdateCarRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CarService {
    Car save(CarRegisterRequest carRegisterRequest, long ownerId) throws IOException;
    List<Car> findAll();
    Car findById(long carId);
    List<Car> findByOwnerId(long ownerId);
    Car update(long carId, UpdateCarRequest carRequest);
    void deleteById(long carId);
    Car saveImage(long carId, MultipartFile file) throws IOException;

    CarOwner findCarOwnerByCarId(long carId);
}
