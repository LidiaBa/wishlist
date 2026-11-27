package com.example.wishlist.controller;

import com.example.wishlist.domain.Booking;
import com.example.wishlist.domain.User;
import com.example.wishlist.domain.Wish;
import com.example.wishlist.dto.BookingDto;
import com.example.wishlist.mapper.BookingMapper;
import com.example.wishlist.service.BookingService;
import com.example.wishlist.service.UserService;
import com.example.wishlist.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController // выходная сущность по rest принципам будет преобразована в json
@RequestMapping("/bookings")
@RequiredArgsConstructor

public class BookingController {

    private final BookingService bookingService;
    private final WishService wishService;
    private final UserService userService;


    @GetMapping
    public List<BookingDto> getMyBookings(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(Objects.requireNonNull(jwt.getSubject(), "ID пользователя не найден в токене"));
        List<Booking> bookings = bookingService.getMyBookings(userId);

        // Вместо ручного создания Map используем маппер
        return bookings.stream().map(booking -> {
            Wish wish = wishService.get(booking.getWishId());
            User owner = userService.get(booking.getOwnerId());
            return BookingMapper.toDto(booking, wish, owner);
        }).toList();
    }

    @PostMapping
    public ResponseEntity<BookingDto> bookWish(
            @RequestParam Long wishId,
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(Objects.requireNonNull(jwt.getSubject(), "ID пользователя не найден в токене"));
        Booking booking = bookingService.book(wishId, userId);

        Wish wish = wishService.get(booking.getWishId());
        User owner = userService.get(booking.getOwnerId());

        return ResponseEntity.status(HttpStatus.CREATED).body(BookingMapper.toDto(booking, wish, owner));
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelBooking(
            @RequestParam Long wishId,
            @AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.valueOf(Objects.requireNonNull(jwt.getSubject(), "ID пользователя не найден в токене"));
        bookingService.cancel(wishId, userId);
        return ResponseEntity.noContent().build();
    }
}
