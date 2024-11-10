package org.example.rentcar.service.review;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.model.Car;
import org.example.rentcar.model.Customer;
import org.example.rentcar.model.Review;
import org.example.rentcar.repository.CustomerRepository;
import org.example.rentcar.repository.ReviewRepository;
import org.example.rentcar.request.ReviewRequest;
import org.example.rentcar.service.car.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CarService carService;
    private final CustomerRepository CustomerRepository;
    @Override
    @Transactional
    public Review saveReview(ReviewRequest reviewRequest,long customerId, long carId) {
        Customer customer = CustomerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        Car car = carService.findById(carId);
        Review review = new Review();
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setCustomer(customer);
        review.setCar(car);
        return reviewRepository.save(review);
    }

    @Override
    public Review getReviewById(long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
    }

    @Override
    public Page<Review> getReviewsByCarId(long carId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return reviewRepository.findAllByCarId(carId, pageRequest);
    }

    @Override
    public Double getAverageRatingByCarId(long carId) {
        List<Review> reviews = getReviewsByCarId(carId, 0, 5).getContent();
        OptionalDouble averageRating = reviews.stream().mapToDouble(Review::getRating).average();
        return averageRating.orElse(0.0);
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Page<Review> getReviewsByCustomerId(long customerId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return reviewRepository.findAllByCustomerId(customerId, pageRequest);
    }

    @Override
    public Review updateReview(ReviewRequest reviewRequest, long reviewId) {
        Review review = getReviewById(reviewId);
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(long reviewId) {
        Review review = getReviewById(reviewId);
        reviewRepository.delete(review);
    }
}
