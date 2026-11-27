package com.example.wishlist.controller;

import com.example.wishlist.domain.Wish;
import com.example.wishlist.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishes")
@RequiredArgsConstructor

public class WishController {

    private final WishService wishService;

    @GetMapping
    public List<Wish> getMyWishes(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return wishService.getByUserId(userId);
    }

    @GetMapping("/user/{userId}")
    public List<Wish> getWishesByUserId(@PathVariable Long userId) {
        return wishService.getByUserId(userId);
    }

    @GetMapping("/{id}")
    public Wish getWish(@PathVariable Long id) {
        return wishService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Wish createWish(@RequestBody Wish wish, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        wish.setUserId(userId);
        return wishService.create(wish);
    }

    @PutMapping("/{id}")
    public Wish updateWish(@PathVariable Long id, @RequestBody Wish wish, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        wish.setId(id);
        return wishService.update(wish, userId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWish(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());
        String userRole = jwt.getClaimAsString("roles"); // Берем роль из токена

        if ("ADMIN".equals(userRole)) {
            wishService.delete(id, null);
        } else {
            wishService.delete(id, userId);
        }
        return ResponseEntity.noContent().build();
    }

}
