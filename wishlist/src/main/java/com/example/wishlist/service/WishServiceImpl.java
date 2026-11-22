package com.example.wishlist.service;

import com.example.wishlist.domain.Wish;
import com.example.wishlist.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishServiceImpl implements WishService {
    private final WishRepository wishRepository;

    public WishServiceImpl(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    @Override
    public Wish create(Wish wish) {
        if (wish.getTitle() == null || wish.getTitle().isBlank()) throw new IllegalArgumentException("Wish title is required");
        if (wish.getUserId() == null) throw new IllegalArgumentException("User ID is required");
        if (wish.getStatus() == null) wish.setStatus("FREE");

        return wishRepository.save(wish);
    }

    @Override
    public Wish get(Long id) {
        if (id == null) throw new IllegalArgumentException("Wish ID is required");
        return wishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wish not found with id: " + id));
    }

    @Override
    public List<Wish> getByUserId(Long userId) {
        if (userId == null) throw new IllegalArgumentException("User ID is required");
        return wishRepository.findByUserId(userId);
    }

    @Override
    public List<Wish> getAll() {
        return wishRepository.findAll();
    }

    @Override
    public Wish update(Wish wish, Long currentUserId) {
        if (wish.getId() == null) throw new IllegalArgumentException("Wish ID is required for update");

        Wish existing = get(wish.getId());
        if (!existing.getUserId().equals(currentUserId)) {
            throw new SecurityException("You can only edit your own wishes");
        }

        wish.setUserId(existing.getUserId()); // Сохраняем владельца
        wish.setStatus(existing.getStatus());// Сохраняем владельца
        return wishRepository.save(wish);
    }

    @Override
    public void delete(Long id, Long currentUserId) {
        if (id == null) throw new IllegalArgumentException("Wish ID is required");
        Wish existing = get(id);

        if (currentUserId != null && !existing.getUserId().equals(currentUserId)) {
            throw new SecurityException("You can only delete your own wishes");
        }

        wishRepository.deleteById(id);
    }

    @Override
    public void bookWish(Long wishId) {
        Wish wish = get(wishId);
        if (!"FREE".equals(wish.getStatus())) throw new IllegalStateException("Wish is already booked");

        wish.setStatus("BOOKED");
        wishRepository.save(wish); // Вместо кастомного updateStatus
    }

    @Override
    public void unbookWish(Long wishId) {
        Wish wish = get(wishId);
        if (!"BOOKED".equals(wish.getStatus())) throw new IllegalStateException("Wish is not booked");

        wish.setStatus("FREE");
        wishRepository.save(wish);
    }
}
