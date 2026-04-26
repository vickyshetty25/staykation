package com.vicky.staykation.controller;

import com.vicky.staykation.model.Availability;
import com.vicky.staykation.model.Property;
import com.vicky.staykation.repository.AvailabilityRepository;
import com.vicky.staykation.repository.PropertyRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    private final AvailabilityRepository availabilityRepository;
    private final PropertyRepository propertyRepository;

    public AvailabilityController(
            AvailabilityRepository availabilityRepository,
            PropertyRepository propertyRepository) {
        this.availabilityRepository = availabilityRepository;
        this.propertyRepository = propertyRepository;
    }

    @PostMapping("/property/{propertyId}")
    public ResponseEntity<?> setAvailability(
            @PathVariable Long propertyId,
            @RequestBody Map<String, Object> body) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        LocalDate date = LocalDate.parse(body.get("date").toString());
        boolean available = Boolean.parseBoolean(
                body.get("available").toString());
        BigDecimal pricePerDay = body.containsKey("pricePerDay")
                ? new BigDecimal(body.get("pricePerDay").toString())
                : property.getBasePrice();

        Availability availability = new Availability();
        availability.setProperty(property);
        availability.setDate(date);
        availability.setAvailable(available);
        availability.setPricePerDay(pricePerDay);

        return ResponseEntity.ok(availabilityRepository.save(availability));
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Availability>> getAvailability(
            @PathVariable Long propertyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {
        return ResponseEntity.ok(
                availabilityRepository.findByPropertyIdAndDateBetween(
                        propertyId, startDate, endDate));
    }
}