package com.example.wishlist.mapper;

import com.example.wishlist.domain.Wish;
import com.example.wishlist.dto.WishDto;

public class WishMapper {
    public static WishDto toDto(Wish wish) {
        if (wish == null) return null;
        return WishDto.builder()
                .id(wish.getId())
                .title(wish.getTitle())
                .link(wish.getLink())
                .price(wish.getPrice())
                .status(wish.getStatus())
                .userId(wish.getUserId())
                .build();
    }
}
