package com.example.wishlist.dto;

import com.example.wishlist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private long expiresIn;
    private User user;
}