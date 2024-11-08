package org.example.rentcar.service.review;

import org.example.rentcar.model.Review;
import org.example.rentcar.request.ReviewRequest;

import java.util.List;

public class ReviewServiceImpl implements ReviewService {
    @Override
    public Review saveReview(Review review) {
        return null;
    }

    @Override
    public Review getReviewById(long id) {
        return null;
    }

    @Override
    public List<Review> getReviewsByCarId(long carId) {
        return List.of();
    }

    @Override
    public List<Review> getAllReviews() {
        return List.of();
    }

    @Override
    public List<Review> getReviewsByCustomerId(long customerId) {
        return List.of();
    }

    @Override
    public Review updateReview(ReviewRequest reviewRequest) {
        return null;
    }

    @Override
    public void deleteReview(long reviewId) {

    }
}
