package com.example.wishlist.mapper;

import com.example.wishlist.domain.User;
import com.example.wishlist.dto.UserDto;

public class UserMapper {
    public static UserDto toDto(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .roles(user.getRoles())
                .build();
    }
}