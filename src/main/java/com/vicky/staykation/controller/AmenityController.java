package com.vicky.staykation.controller;

import com.vicky.staykation.model.Amenity;
import com.vicky.staykation.model.Property;
import com.vicky.staykation.repository.AmenityRepository;
import com.vicky.staykation.repository.PropertyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityRepository amenityRepository;
    private final PropertyRepository propertyRepository;

    public AmenityController(AmenityRepository amenityRepository,
                             PropertyRepository propertyRepository) {
        this.amenityRepository = amenityRepository;
        this.propertyRepository = propertyRepository;
    }

    @PostMapping("/property/{propertyId}")
    public ResponseEntity<Amenity> addAmenity(
            @PathVariable Long propertyId,
            @RequestBody Map<String, String> body) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        Amenity amenity = new Amenity();
        amenity.setProperty(property);
        amenity.setName(body.get("name"));
        return ResponseEntity.ok(amenityRepository.save(amenity));
    }

    @DeleteMapping("/{amenityId}")
    public ResponseEntity<?> removeAmenity(@PathVariable Long amenityId) {
        amenityRepository.deleteById(amenityId);
        return ResponseEntity.ok(Map.of("message", "Amenity removed"));
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Amenity>> getAmenities(
            @PathVariable Long propertyId) {
        return ResponseEntity.ok(
                amenityRepository.findByPropertyId(propertyId));
    }
}