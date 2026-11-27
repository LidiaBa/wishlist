package com.example.wishlist.controller;

import com.example.wishlist.domain.Wish;
import com.example.wishlist.dto.WishDto;
import com.example.wishlist.mapper.WishMapper;
import com.example.wishlist.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/wishes")
@RequiredArgsConstructor

public class WishController {

    private final WishService wishService;

    @GetMapping
    public List<WishDto> getMyWishes(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(Objects.requireNonNull(jwt.getSubject()));
        return wishService.getByUserId(userId)
                .stream()
                .map(WishMapper::toDto)
                .toList();
    }

    @GetMapping("/user/{userId}")
    public List<WishDto> getWishesByUserId(@PathVariable Long userId) {
        return wishService.getByUserId(userId)
                .stream()
                .map(WishMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public WishDto getWish(@PathVariable Long id) {

        return WishMapper.toDto(wishService.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WishDto createWish(@RequestBody Wish wish, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(Objects.requireNonNull(jwt.getSubject()));
        wish.setUserId(userId);
        return WishMapper.toDto(wishService.create(wish));
    }

    @PutMapping("/{id}")
    public WishDto updateWish(@PathVariable Long id, @RequestBody Wish wish, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(Objects.requireNonNull(jwt.getSubject(), "ID пользователя не найден в токене"));
        wish.setId(id);
        return WishMapper.toDto(wishService.update(wish, userId));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWish(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.valueOf(Objects.requireNonNull(jwt.getSubject(), "ID пользователя не найден в токене"));
        String userRole = jwt.getClaimAsString("roles"); // Берем роль из токена

        if ("ADMIN".equals(userRole)) {
            wishService.delete(id, null);
        } else {
            wishService.delete(id, userId);
        }
        return ResponseEntity.noContent().build();
    }

}
