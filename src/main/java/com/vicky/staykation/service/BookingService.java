package com.vicky.staykation.service;

import com.vicky.staykation.model.*;
import com.vicky.staykation.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,
                          PropertyRepository propertyRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }

    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Booking createBooking(Long propertyId,
                                 LocalDate startDate,
                                 LocalDate endDate,
                                 String modeOfPayment) {
        User guest = getLoggedInUser();

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (bookingRepository.isPropertyBooked(
                propertyId, startDate, endDate)) {
            throw new RuntimeException(
                    "Property not available for selected dates");
        }

        long nights = ChronoUnit.DAYS.between(startDate, endDate);
        if (nights <= 0) {
            throw new RuntimeException("Invalid dates");
        }

        BigDecimal totalPrice = property.getBasePrice()
                .multiply(BigDecimal.valueOf(nights));

        Booking booking = new Booking();
        booking.setGuest(guest);
        booking.setProperty(property);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setTotalPrice(totalPrice);
        booking.setModeOfPayment(modeOfPayment);
        booking.setStatus("CONFIRMED");

        // Simulate payment
        booking.setPaymentId("PAY-" + UUID.randomUUID()
                .toString().substring(0, 8).toUpperCase());
        booking.setPaymentStatus("PAID");

        return bookingRepository.save(booking);
    }

    public List<Booking> getMyBookings() {
        return bookingRepository.findByGuestIdOrderByCreatedAtDesc(
                getLoggedInUser().getId());
    }

    public List<Booking> getPropertyBookings(Long propertyId) {
        return bookingRepository
                .findByPropertyIdOrderByCreatedAtDesc(propertyId);
    }

    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getGuest().getId().equals(getLoggedInUser().getId())) {
            throw new RuntimeException("You do not own this booking");
        }

        booking.setStatus("CANCELLED");
        booking.setPaymentStatus("REFUNDED");
        return bookingRepository.save(booking);
    }

    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus("CONFIRMED");
        return bookingRepository.save(booking);
    }

    public Booking completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus("COMPLETED");
        return bookingRepository.save(booking);
    }
}