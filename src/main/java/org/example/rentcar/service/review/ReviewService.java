package org.example.rentcar.service.review;

import org.example.rentcar.model.Review;
import org.example.rentcar.request.ReviewRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {
    Review saveReview(Review review);
    Review getReviewById(long id);
    Page<Review> getReviewsByCarId(long carId, int page, int size);
    Double getAverageRatingByCarId(long carId);
    List<Review> getAllReviews();
    Page<Review> getReviewsByCustomerId(long customerId, int page, int size);
    Review updateReview(ReviewRequest reviewRequest);
    void deleteReview(long reviewId);
}
