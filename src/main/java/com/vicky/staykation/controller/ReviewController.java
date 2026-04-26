package com.vicky.staykation.controller;

import com.vicky.staykation.model.Review;
import com.vicky.staykation.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Review> createReview(
            @RequestBody Map<String, Object> body) {
        Long propertyId = Long.valueOf(body.get("propertyId").toString());
        Integer rating = Integer.valueOf(body.get("rating").toString());
        String comment = body.get("comment").toString();
        return ResponseEntity.ok(
                reviewService.createReview(propertyId, rating, comment));
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Review>> getPropertyReviews(
            @PathVariable Long propertyId) {
        return ResponseEntity.ok(
                reviewService.getPropertyReviews(propertyId));
    }
}