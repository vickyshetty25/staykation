package com.vicky.staykation.controller;

import com.vicky.staykation.model.Booking;
import com.vicky.staykation.repository.BookingRepository;
import com.vicky.staykation.service.BookingService;
import com.vicky.staykation.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final BookingRepository bookingRepository;

    public BookingController(BookingService bookingService,
                             PaymentService paymentService,
                             BookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.paymentService = paymentService;
        this.bookingRepository = bookingRepository;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestBody Map<String, String> body) {
        Long propertyId = Long.valueOf(body.get("propertyId"));
        LocalDate startDate = LocalDate.parse(body.get("startDate"));
        LocalDate endDate = LocalDate.parse(body.get("endDate"));
        String modeOfPayment = body.getOrDefault("modeOfPayment", "CARD");
        return ResponseEntity.ok(bookingService.createBooking(
                propertyId, startDate, endDate, modeOfPayment));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<Booking>> getMyBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Booking>> getPropertyBookings(
            @PathVariable Long propertyId) {
        return ResponseEntity.ok(
                bookingService.getPropertyBookings(propertyId));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBooking(
            @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException(
                        "Booking not found")));
    }

    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }

    @PutMapping("/{bookingId}/confirm")
    public ResponseEntity<Booking> confirmBooking(
            @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.confirmBooking(bookingId));
    }

    @PutMapping("/{bookingId}/complete")
    public ResponseEntity<Booking> completeBooking(
            @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.completeBooking(bookingId));
    }
}