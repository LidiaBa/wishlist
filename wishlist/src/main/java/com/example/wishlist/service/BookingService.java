package com.example.wishlist.service;

import com.example.wishlist.domain.Booking;
import java.util.List;

public interface BookingService {
    Booking book(Long wishId, Long currentUserId);
    void cancel(Long wishId, Long currentUserId);
    List<Booking> getMyBookings(Long currentUserId);
    List<Booking> getBookingsForMe(Long currentUserId);
    Booking getByWishId(Long wishId);
    boolean isBooked(Long wishId);
}
