package com.vicky.staykation.service;

import com.vicky.staykation.model.*;
import com.vicky.staykation.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         PropertyRepository propertyRepository,
                         UserRepository userRepository,
                         BookingRepository bookingRepository) {
        this.reviewRepository = reviewRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Review createReview(Long propertyId,
                               Integer rating,
                               String comment) {
        User guest = getLoggedInUser();

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        Review review = new Review();
        review.setGuest(guest);
        review.setProperty(property);
        review.setRating(rating);
        review.setComment(comment);
        reviewRepository.save(review);

        // Update average rating on property
        List<Review> allReviews = reviewRepository
                .findByPropertyIdOrderByCreatedAtDesc(propertyId);
        double avg = allReviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        property.setAverageRating(Math.round(avg * 10.0) / 10.0);
        property.setTotalReviews(allReviews.size());
        propertyRepository.save(property);

        return review;
    }

    public List<Review> getPropertyReviews(Long propertyId) {
        return reviewRepository.findByPropertyIdOrderByCreatedAtDesc(propertyId);
    }
}