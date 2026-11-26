package com.example.wishlist.service;

import com.example.wishlist.domain.Wish;
import com.example.wishlist.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {
    private final WishRepository wishRepository;

    @Override
    public Wish create(Wish wish) {

        if (wish.getTitle() == null || wish.getTitle().isBlank()) throw new IllegalArgumentException("Wish title is required");
        if (wish.getUserId() == null) throw new IllegalArgumentException("User ID is required");
        if (wish.getStatus() == null) wish.setStatus("FREE");

        return wishRepository.save(wish);
    }

    @Override
    public Wish get(Long id) {
        log.debug("Finding wish by id {} ", id);
        if (id == null) throw new IllegalArgumentException("Wish ID is required");
        return wishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wish not found with id: " + id));
    }

    @Override
    public List<Wish> getByUserId(Long userId) {
        log.debug("Finding user by id {}", userId);
        if (userId == null) throw new IllegalArgumentException("User ID is required");
        return wishRepository.findByUserId(userId);
    }

    @Override
    public Wish update(Wish wish, Long currentUserId) {

        if (wish.getId() == null) throw new IllegalArgumentException("Wish ID is required for update");

        Wish existing = get(wish.getId());
        if (!existing.getUserId().equals(currentUserId)) {
            throw new SecurityException("You can only edit your own wishes");
        }

        wish.setUserId(existing.getUserId()); // Сохраняем владельца
        wish.setStatus(existing.getStatus());// Сохраняем статус
        return wishRepository.save(wish);
    }

    @Override
    public void delete(Long id, Long currentUserId) {

        log.debug("Delete wish by id {} from user {}", id, currentUserId );

        if (id == null) throw new IllegalArgumentException("Wish ID is required");

        Wish existing = get(id);
        if (currentUserId != null && !existing.getUserId().equals(currentUserId)) {
            throw new SecurityException("You can only delete your own wishes");
        }
        wishRepository.deleteById(id);
    }
}
