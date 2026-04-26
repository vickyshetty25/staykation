package com.vicky.staykation.repository;

import com.vicky.staykation.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByGuestIdOrderByCreatedAtDesc(Long guestId);
    List<Booking> findByPropertyIdOrderByCreatedAtDesc(Long propertyId);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.property.id = :propertyId " +
            "AND b.status != 'CANCELLED' " +
            "AND (b.startDate <= :endDate AND b.endDate >= :startDate)")
    boolean isPropertyBooked(@Param("propertyId") Long propertyId,
                             @Param("startDate") LocalDate startDate,
                             @Param("endDate") LocalDate endDate);
    List<Booking> findByPropertyIdAndStatus(Long propertyId, String status);

}