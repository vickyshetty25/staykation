package com.vicky.staykation.repository;

import com.vicky.staykation.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    List<Amenity> findByPropertyId(Long propertyId);
}