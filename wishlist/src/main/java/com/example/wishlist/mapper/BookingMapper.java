package com.example.wishlist.mapper;

import com.example.wishlist.domain.Booking;
import com.example.wishlist.domain.User;
import com.example.wishlist.domain.Wish;
import com.example.wishlist.dto.BookingDto;

public class BookingMapper {
    public static BookingDto toDto(Booking booking, Wish wish, User owner) {
        return BookingDto.builder()
                .id(booking.getId())
                .wishId(booking.getWishId())
                .userId(booking.getUserId())
                .ownerId(booking.getOwnerId())
                // Используем другие мапперы!
                .wish(WishMapper.toDto(wish))
                .owner(UserMapper.toDto(owner))
                .build();
    }
}