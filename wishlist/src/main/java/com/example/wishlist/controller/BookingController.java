package com.example.wishlist.controller;

import com.example.wishlist.domain.Booking;
import com.example.wishlist.domain.User;
import com.example.wishlist.domain.Wish;
import com.example.wishlist.service.BookingService;
import com.example.wishlist.service.UserService;
import com.example.wishlist.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // выходная сущность по rest принципам будет преобразована в json
@RequestMapping("/bookings")
@RequiredArgsConstructor

public class BookingController {

    private final BookingService bookingService;
    private final WishService wishService;
    private final UserService userService;


    @GetMapping
    public List<Map<String, Object>> getMyBookings(@RequestAttribute("userId") Long userId) {
        List<Booking> bookings = bookingService.getMyBookings(userId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Booking booking : bookings) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", booking.getId());
            item.put("wishId", booking.getWishId());
            item.put("userId", booking.getUserId());
            item.put("ownerId", booking.getOwnerId());

            Wish wish = wishService.get(booking.getWishId());
            if (wish != null) {
                item.put("wishTitle", wish.getTitle());
                item.put("wishLink", wish.getLink());
                item.put("wishPrice", wish.getPrice());
            }

            User owner = userService.get(booking.getOwnerId());
            if (owner != null) {
                item.put("ownerName", owner.getName());
            }
            result.add(item);
        }
        return result;
    }

    @PostMapping
    public ResponseEntity<Booking> bookWish(
            @RequestParam Long wishId,
            @RequestAttribute("userId") Long userId) {
        Booking booking = bookingService.book(wishId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelBooking(
            @RequestParam Long wishId,
            @RequestAttribute("userId") Long userId) {
        bookingService.cancel(wishId, userId);
        return ResponseEntity.noContent().build();
    }
}
