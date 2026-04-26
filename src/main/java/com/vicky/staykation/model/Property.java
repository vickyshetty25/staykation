package com.vicky.staykation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "properties")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal basePrice;

    @Column(nullable = false)
    private String location;

    private String propertyType;
    private Integer maxGuests;
    private Integer bedrooms;
    private Integer bathrooms;

    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @Column(nullable = false)
    private String status = "ACTIVE";

    private Double latitude;
    private Double longitude;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "host_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler",
            "password", "bookings"})
    private User host;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<PropertyImage> images;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<Amenity> amenities;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}