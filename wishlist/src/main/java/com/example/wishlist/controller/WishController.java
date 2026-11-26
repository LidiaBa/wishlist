package com.example.wishlist.controller;

import com.example.wishlist.domain.Wish;
import com.example.wishlist.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishes")
@RequiredArgsConstructor

public class WishController {

    private final WishService wishService;

    @GetMapping
    public List<Wish> getMyWishes(@RequestAttribute("userId") Long userId) {
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
    public Wish createWish(@RequestBody Wish wish, @RequestAttribute("userId") Long userId) {
        wish.setUserId(userId);
        return wishService.create(wish);
    }

    @PutMapping("/{id}")
    public Wish updateWish(@PathVariable Long id, @RequestBody Wish wish, @RequestAttribute("userId") Long userId) {
        wish.setId(id);
        return wishService.update(wish, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWish(@PathVariable Long id,
                                           @RequestAttribute("userId") Long userId,
                                           @RequestAttribute("userRole") String userRole)
    {

        if ("ADMIN".equals(userRole)) {
            wishService.delete(id, null);
        } else {
            wishService.delete(id, userId);
        }
        return ResponseEntity.noContent().build();
    }
}
