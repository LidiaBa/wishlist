package com.example.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishDto {
    private Long id;
    private String title;
    private String link;
    private Integer price;
    private String status;
    private Long userId;
}