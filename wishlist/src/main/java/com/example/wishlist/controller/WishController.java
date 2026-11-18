package com.example.wishlist.controller;

import com.example.wishlist.domain.Wish;
import com.example.wishlist.service.WishService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public List<Wish> getMyWishes(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return wishService.getByUserId(userId);
    }

    @GetMapping("/{id}")
    public Wish getWish(@PathVariable Long id) {
        return wishService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Wish createWish(@RequestBody Wish wish, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        wish.setUserId(userId);
        return wishService.create(wish);
    }

    @PutMapping("/{id}")
    public Wish updateWish(@PathVariable Long id, @RequestBody Wish wish, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        wish.setId(id);
        return wishService.update(wish, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWish(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String userRole = (String) request.getAttribute("userRole");

        if ("ADMIN".equals(userRole)) {
            wishService.delete(id, null);
        } else {
            wishService.delete(id, userId);
        }
        return ResponseEntity.noContent().build();
    }
}
