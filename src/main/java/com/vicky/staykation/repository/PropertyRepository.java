package com.vicky.staykation.repository;

import com.vicky.staykation.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByHostId(Long hostId);

    @Query("SELECT p FROM Property p WHERE p.status = 'ACTIVE' " +
            "AND (:location IS NULL OR LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:minPrice IS NULL OR p.basePrice >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.basePrice <= :maxPrice) " +
            "AND (:propertyType IS NULL OR p.propertyType = :propertyType) " +
            "AND (:guests IS NULL OR p.maxGuests >= :guests)")
    List<Property> searchProperties(
            @Param("location") String location,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("propertyType") String propertyType,
            @Param("guests") Integer guests);
}