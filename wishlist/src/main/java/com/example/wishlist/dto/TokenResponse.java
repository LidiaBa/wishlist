package com.example.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private long expiresIn;
    private UserDto user;
}