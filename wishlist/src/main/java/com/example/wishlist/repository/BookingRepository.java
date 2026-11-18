package com.example.wishlist.repository;

import com.example.wishlist.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByWishId(Long wishId);
    List<Booking> findByUserId(Long userId);
    List<Booking> findByOwnerId(Long ownerId);
    void deleteByWishId(Long wishId);
}