package com.example.wishlist.controller;

import com.example.wishlist.dto.Token;
import com.example.wishlist.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> login(@RequestParam String login, @RequestParam String password) {
        if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Не передан login или password"));
        }

        Token token = authService.auth(login, password);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Не найдено пользователя"));
        }
        return ResponseEntity.ok(Map.of("token", token));
    }
}