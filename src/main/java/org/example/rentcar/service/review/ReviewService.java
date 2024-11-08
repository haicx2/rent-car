package org.example.rentcar.service.review;

import org.example.rentcar.model.Review;
import org.example.rentcar.request.ReviewRequest;

import java.util.List;

public interface ReviewService {
    Review saveReview(Review review);
    Review getReviewById(long id);
    List<Review> getReviewsByCarId(long carId);
    List<Review> getAllReviews();
    List<Review> getReviewsByCustomerId(long customerId);
    Review updateReview(ReviewRequest reviewRequest);
    void deleteReview(long reviewId);
}
