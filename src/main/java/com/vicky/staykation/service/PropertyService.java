package com.vicky.staykation.service;

import com.vicky.staykation.model.*;
import com.vicky.staykation.repository.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public PropertyService(PropertyRepository propertyRepository,
                           UserRepository userRepository,
                           ReviewRepository reviewRepository) {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Property createProperty(Property property) {
        User host = getLoggedInUser();
        if (!host.isHost()) {
            throw new RuntimeException("Only hosts can list properties");
        }
        property.setHost(host);
        return propertyRepository.save(property);
    }

    @Cacheable(value = "properties", key = "#location + #minPrice + #maxPrice + #propertyType + #guests")
    public List<Property> searchProperties(String location,
                                           BigDecimal minPrice,
                                           BigDecimal maxPrice,
                                           String propertyType,
                                           Integer guests) {
        return propertyRepository.searchProperties(
                location, minPrice, maxPrice, propertyType, guests);
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    public List<Property> getMyProperties() {
        return propertyRepository.findByHostId(getLoggedInUser().getId());
    }

    public Property updateProperty(Long id, Property updated) {
        Property property = getPropertyById(id);
        if (!property.getHost().getId().equals(getLoggedInUser().getId())) {
            throw new RuntimeException("You do not own this property");
        }
        property.setTitle(updated.getTitle());
        property.setDescription(updated.getDescription());
        property.setBasePrice(updated.getBasePrice());
        property.setLocation(updated.getLocation());
        property.setPropertyType(updated.getPropertyType());
        property.setMaxGuests(updated.getMaxGuests());
        property.setBedrooms(updated.getBedrooms());
        property.setBathrooms(updated.getBathrooms());
        return propertyRepository.save(property);
    }

    public void deleteProperty(Long id) {
        Property property = getPropertyById(id);
        if (!property.getHost().getId().equals(getLoggedInUser().getId())) {
            throw new RuntimeException("You do not own this property");
        }
        property.setStatus("INACTIVE");
        propertyRepository.save(property);
    }

    public Double getAverageRating(Long propertyId) {
        return reviewRepository.getAverageRating(propertyId);
    }
}