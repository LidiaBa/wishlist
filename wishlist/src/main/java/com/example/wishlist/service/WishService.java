package com.example.wishlist.service;

import com.example.wishlist.domain.Wish;
import java.util.List;

public interface WishService {
    Wish create(Wish wish);
    Wish get(Long id);
    List<Wish> getByUserId(Long userId);
    Wish update(Wish wish, Long currentUserId);
    void delete(Long id, Long currentUserId);
}
