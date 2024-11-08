package org.example.rentcar.service.review;

import org.example.rentcar.model.Review;
import org.example.rentcar.request.ReviewRequest;
import org.springframework.data.domain.Page;

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
    public Page<Review> getReviewsByCarId(long carId, int page, int size) {
        return null;
    }

    @Override
    public Double getAverageRatingByCarId(long carId) {
        return 0.0;
    }

    @Override
    public List<Review> getAllReviews() {
        return List.of();
    }

    @Override
    public Page<Review> getReviewsByCustomerId(long customerId, int page, int size) {
        return null;
    }

    @Override
    public Review updateReview(ReviewRequest reviewRequest) {
        return null;
    }

    @Override
    public void deleteReview(long reviewId) {

    }
}
