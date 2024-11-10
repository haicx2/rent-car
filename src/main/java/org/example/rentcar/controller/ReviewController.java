package org.example.rentcar.controller;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.model.Review;
import org.example.rentcar.request.ReviewRequest;
import org.example.rentcar.response.APIResponse;
import org.example.rentcar.service.review.ReviewService;
import org.example.rentcar.utils.FeedBackMessage;
import org.example.rentcar.utils.UrlMapping;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.REVIEW)
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping(UrlMapping.ADD)
    public ResponseEntity<APIResponse> createReview(@RequestBody ReviewRequest reviewRequest,
                                                    @RequestParam long customerId,
                                                    @RequestParam long carId) {
        Review review = reviewService.saveReview(reviewRequest,customerId,carId);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, review));
    }

    @GetMapping(UrlMapping.GET_BY_ID)
    public ResponseEntity<APIResponse> getReviewById(@PathVariable long id) {
        Review review = reviewService.getReviewById(id);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.FOUND, review));
    }

    @GetMapping(UrlMapping.GET_ALL)
    public ResponseEntity<APIResponse> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.FOUND, reviews));
    }

    @GetMapping(UrlMapping.GET_BY_CAR_ID)
    public ResponseEntity<APIResponse> getReviewByCarId(@PathVariable long carId,
                                                        @RequestParam(defaultValue = "0") int size,
                                                        @RequestParam(defaultValue = "5") int page) {
        Page<Review> reviews = reviewService.getReviewsByCarId(carId,size,page);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.FOUND, reviews));
    }

    @GetMapping(UrlMapping.GET_BY_CUSTOMER_ID)
    public ResponseEntity<APIResponse> getReviewsByCustomerId(@PathVariable long customerId,
                                                              @RequestParam(defaultValue = "0") int size,
                                                              @RequestParam(defaultValue = "5") int page) {
        Page<Review> reviews = reviewService.getReviewsByCustomerId(customerId,size,page);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.FOUND, reviews));
    }

    @PutMapping(UrlMapping.UPDATE_BY_ID)
    public ResponseEntity<APIResponse> updateReviewById(@PathVariable long id,
                                                        @RequestBody ReviewRequest reviewRequest) {
        Review review = reviewService.updateReview(reviewRequest, id);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.UPDATE_SUCCESS, review));
    }

    @DeleteMapping(UrlMapping.DELETE_BY_ID)
    public ResponseEntity<APIResponse> deleteReviewById(@PathVariable long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
