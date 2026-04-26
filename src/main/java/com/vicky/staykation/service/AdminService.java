package com.vicky.staykation.service;

import com.vicky.staykation.repository.*;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    public AdminService(UserRepository userRepository,
                        PropertyRepository propertyRepository,
                        BookingRepository bookingRepository,
                        ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
    }

    public Map<String, Object> getAnalytics() {
        long totalUsers = userRepository.count();
        long totalProperties = propertyRepository.count();
        long totalBookings = bookingRepository.count();
        long totalReviews = reviewRepository.count();

        return Map.of(
                "totalUsers", totalUsers,
                "totalProperties", totalProperties,
                "totalBookings", totalBookings,
                "totalReviews", totalReviews
        );
    }
}