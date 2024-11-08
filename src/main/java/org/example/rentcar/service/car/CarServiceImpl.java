package org.example.rentcar.service.car;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.exception.ResourceNotFoundException;
import org.example.rentcar.model.Car;
import org.example.rentcar.model.CarOwner;
import org.example.rentcar.repository.CarOwnerRepository;
import org.example.rentcar.repository.CarRepository;
import org.example.rentcar.request.CarRegisterRequest;
import org.example.rentcar.request.UpdateCarRequest;
import org.example.rentcar.utils.FeedBackMessage;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarOwnerRepository carOwnerRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Car save(CarRegisterRequest carRegisterRequest, long ownerId) {
        Car car = modelMapper.map(carRegisterRequest, Car.class);
        CarOwner owner = carOwnerRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException("Car owner not found"));
        car.setOwner(owner);
        return carRepository.save(car);
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public Car findById(long carId) {
        return carRepository.findById(carId).orElseThrow(() -> new ResourceNotFoundException(FeedBackMessage.NOT_FOUND));
    }

    @Override
    public List<Car> findByOwnerId(long ownerId) {
        return carRepository.findAllByOwnerId(ownerId);
    }

    @Override
    @Transactional
    public Car update(long carId, UpdateCarRequest carRequest) {
        Car car = findById(carId);
        modelMapper.map(carRequest, car);
        return carRepository.save(car);
    }

    @Override
    public void deleteById(long carId) {
        Car car = findById(carId);
        carRepository.delete(car);
    }

    @Override
    @Transactional
    public Car saveImage(long carId, MultipartFile file) throws IOException {
        Car car = findById(carId);
        car.setImage(file.getBytes());
        return carRepository.save(car);
    }

    @Override
    public CarOwner findCarOwnerByCarId(long carId) {
        Car car = findById(carId);
        return carOwnerRepository.findById(car.getOwner().getId()).orElseThrow(() -> new ResourceNotFoundException("Car owner not found"));
    }
}
