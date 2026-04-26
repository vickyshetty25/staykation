package com.vicky.staykation.repository;

import com.vicky.staykation.model.Wishlist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);
    Optional<Wishlist> findByUserIdAndPropertyId(Long userId, Long propertyId);

    @Transactional
    void deleteByUserIdAndPropertyId(Long userId, Long propertyId);
}