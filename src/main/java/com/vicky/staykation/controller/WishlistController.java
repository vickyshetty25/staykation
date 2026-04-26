package com.vicky.staykation.controller;

import com.vicky.staykation.model.*;
import com.vicky.staykation.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    public WishlistController(WishlistRepository wishlistRepository,
                              UserRepository userRepository,
                              PropertyRepository propertyRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/add/{propertyId}")
    public ResponseEntity<?> addToWishlist(
            @PathVariable Long propertyId) {
        User user = getLoggedInUser();
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (wishlistRepository.findByUserIdAndPropertyId(
                user.getId(), propertyId).isPresent()) {
            return ResponseEntity.ok(
                    Map.of("message", "Already in wishlist"));
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProperty(property);
        wishlistRepository.save(wishlist);

        return ResponseEntity.ok(Map.of("message", "Added to wishlist"));
    }

    @DeleteMapping("/remove/{propertyId}")
    public ResponseEntity<?> removeFromWishlist(
            @PathVariable Long propertyId) {
        User user = getLoggedInUser();
        wishlistRepository.deleteByUserIdAndPropertyId(
                user.getId(), propertyId);
        return ResponseEntity.ok(Map.of("message", "Removed from wishlist"));
    }

    @GetMapping
    public ResponseEntity<List<Wishlist>> getWishlist() {
        return ResponseEntity.ok(
                wishlistRepository.findByUserId(getLoggedInUser().getId()));
    }
}