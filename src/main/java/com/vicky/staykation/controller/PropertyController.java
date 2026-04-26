package com.vicky.staykation.controller;

import com.vicky.staykation.model.Property;
import com.vicky.staykation.service.PropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    public ResponseEntity<Property> createProperty(
            @RequestBody Property property) {
        return ResponseEntity.ok(propertyService.createProperty(property));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchProperties(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) Integer guests) {
        return ResponseEntity.ok(propertyService.searchProperties(
                location, minPrice, maxPrice, propertyType, guests));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProperty(@PathVariable Long id) {
        Property property = propertyService.getPropertyById(id);
        Double avgRating = propertyService.getAverageRating(id);
        return ResponseEntity.ok(Map.of(
                "property", property,
                "averageRating", avgRating != null
                        ? avgRating : 0.0
        ));
    }

    @GetMapping("/my-properties")
    public ResponseEntity<List<Property>> getMyProperties() {
        return ResponseEntity.ok(propertyService.getMyProperties());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(
            @PathVariable Long id,
            @RequestBody Property property) {
        return ResponseEntity.ok(
                propertyService.updateProperty(id, property));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok(
                Map.of("message", "Property deactivated successfully"));
    }
}