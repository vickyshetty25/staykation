package com.vicky.staykation.repository;

import com.vicky.staykation.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AvailabilityRepository
        extends JpaRepository<Availability, Long> {
    List<Availability> findByPropertyIdAndDateBetween(
            Long propertyId, LocalDate start, LocalDate end);
}