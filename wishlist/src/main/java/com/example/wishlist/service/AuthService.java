package com.example.wishlist.service;

import com.example.wishlist.domain.User;
import com.example.wishlist.dto.Token;
import com.example.wishlist.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    private final UserRepository userRepository;

    private final Map<String, Token> tokens = new ConcurrentHashMap<>();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Аналог auth(login, password)
    public Token auth(String login, String password) {
        User user = userRepository.findByLogin(login).orElse(null);
        if (user != null && user.getPassword().equals(password)) {
            return generate(user);
        }
        return null;
    }

    // Аналог auth(token)
    public User auth(String token) {
        Token t = tokens.get(token);
        if (t != null && t.getExp().isAfter(LocalDateTime.now())) {
            return t.getUser();
        }
        return null;
    }

    private Token generate(User user) {
        String tokenStr = UUID.randomUUID().toString();
        Token t = Token.builder()
                .user(user)
                .exp(LocalDateTime.now().plusHours(4))
                .token(tokenStr)
                .build();
        tokens.put(tokenStr, t);
        return t;
    }
}