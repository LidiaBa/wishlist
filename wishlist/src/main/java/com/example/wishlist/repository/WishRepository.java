package com.example.wishlist.repository;

import com.example.wishlist.domain.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByUserId(Long userId);
}
