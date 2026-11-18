package com.example.wishlist.service;

import com.example.wishlist.domain.Booking;
import com.example.wishlist.domain.Wish;
import com.example.wishlist.repository.BookingRepository;
import com.example.wishlist.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final WishRepository wishRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, WishRepository wishRepository) {
        this.bookingRepository = bookingRepository;
        this.wishRepository = wishRepository;
    }

    @Override
    @Transactional // Очень важно! Гарантирует целостность данных
    public Booking book(Long wishId, Long currentUserId) {
        if (wishId == null) throw new IllegalArgumentException("Wish ID is required");
        if (currentUserId == null) throw new IllegalArgumentException("User ID is required");

        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new RuntimeException("Wish not found with id: " + wishId));

        if (wish.getUserId().equals(currentUserId)) throw new IllegalAccessError("You cannot book your own wish");
        if (!"FREE".equals(wish.getStatus())) throw new IllegalStateException("This wish is already booked");

        Booking booking = Booking.builder()
                .wishId(wishId)
                .userId(currentUserId)
                .ownerId(wish.getUserId())
                .build();

        booking = bookingRepository.save(booking);

        wish.setStatus("BOOKED");
        wishRepository.save(wish);

        return booking;
    }

    @Override
    @Transactional
    public void cancel(Long wishId, Long currentUserId) {
        if (wishId == null) throw new IllegalArgumentException("Wish ID is required");
        if (currentUserId == null) throw new IllegalArgumentException("User ID is required");

        List<Booking> bookings = bookingRepository.findByWishId(wishId);
        if (bookings.isEmpty()) throw new RuntimeException("No booking found for wish: " + wishId);

        Booking booking = bookings.get(0);
        if (!booking.getUserId().equals(currentUserId)) throw new SecurityException("You can only cancel your own bookings");

        bookingRepository.deleteByWishId(wishId);

        Wish wish = wishRepository.findById(wishId).orElse(null);
        if (wish != null) {
            wish.setStatus("FREE");
            wishRepository.save(wish);
        }
    }

    @Override
    public List<Booking> getMyBookings(Long currentUserId) {
        if (currentUserId == null) throw new IllegalArgumentException("User ID is required");
        return bookingRepository.findByUserId(currentUserId);
    }

    @Override
    public List<Booking> getBookingsForMe(Long currentUserId) {
        if (currentUserId == null) throw new IllegalArgumentException("User ID is required");
        return bookingRepository.findByOwnerId(currentUserId);
    }

    @Override
    public Booking getByWishId(Long wishId) {
        if (wishId == null) throw new IllegalArgumentException("Wish ID is required");
        List<Booking> bookings = bookingRepository.findByWishId(wishId);
        return bookings.isEmpty() ? null : bookings.get(0);
    }

    @Override
    public boolean isBooked(Long wishId) {
        return getByWishId(wishId) != null;
    }
}
