package org.example.rentcar.controller;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.model.Car;
import org.example.rentcar.request.CarRegisterRequest;
import org.example.rentcar.request.UpdateCarRequest;
import org.example.rentcar.response.APIResponse;
import org.example.rentcar.service.car.CarService;
import org.example.rentcar.utils.FeedBackMessage;
import org.example.rentcar.utils.UrlMapping;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(UrlMapping.CAR)
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping(UrlMapping.GET_BY_ID)
    public ResponseEntity<APIResponse> getCarById(@PathVariable int id) {
        Car car = carService.findById(id);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.FOUND, car));
    }

    @GetMapping(UrlMapping.GET_ALL)
    public ResponseEntity<APIResponse> getAllCars(@RequestParam int size,
                                                  @RequestParam int page) {
        Page<Car> cars = carService.findAll(size,page);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.FOUND, cars));
    }

    @GetMapping(UrlMapping.GET_BY_OWNER_ID)
    public ResponseEntity<APIResponse> getAllCarByOwnerID(@PathVariable long ownerId) {
        List<Car> cars = carService.findByOwnerId(ownerId);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.FOUND, cars));
    }

    @PostMapping(UrlMapping.ADD)
    public ResponseEntity<APIResponse> addCar(@RequestBody CarRegisterRequest carRegisterRequest,
                                              @RequestParam long ownerId) throws IOException {
        Car savedCar = carService.save(carRegisterRequest, ownerId);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, savedCar));
    }

    @PutMapping(UrlMapping.UPDATE_BY_ID)
    public ResponseEntity<APIResponse> updateCarById(@PathVariable int id, @RequestBody UpdateCarRequest carRequest) {
        Car car = carService.update(id, carRequest);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, car));
    }

    @PutMapping(UrlMapping.UPDATE_IMAGE_BY_ID)
    public ResponseEntity<APIResponse> updateImageById(@PathVariable long carId, @RequestBody MultipartFile image) throws IOException {
        Car car = carService.saveImage(carId, image);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.UPDATE_SUCCESS, car));
    }

    @DeleteMapping(UrlMapping.DELETE_BY_ID)
    public ResponseEntity<APIResponse> deleteCarById(@PathVariable int id) {
        carService.deleteById(id);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.DELETE_SUCCESS, null));
    }
}
