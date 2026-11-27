package com.example.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private Long wishId;
    private Long userId;
    private Long ownerId;

    private WishDto wish;
    private UserDto owner;
}