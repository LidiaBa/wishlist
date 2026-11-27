package com.example.wishlist.controller;
import com.example.wishlist.domain.User;
import com.example.wishlist.dto.TokenResponse;
import com.example.wishlist.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    @Value("${jwt.ttl}")
    private Duration ttl;

    public record LoginRequest(String username, String password) {}

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest loginRequest) {
        //Проверяем логин и пароль
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        User user = (User) Objects.requireNonNull(authentication.getPrincipal(), "User not found");

        var now = Instant.now();

        // Формируем полезную нагрузку (claims) для токена
        var claim = JwtClaimsSet.builder()
                .issuer("wishlist")
                .subject(String.valueOf(user.getId())) // Сюда попадет login пользователя
                .issuedAt(now)
                .expiresAt(now.plus(ttl))
                .claim("roles", user.getRoles())
                .build();

        // Кодируем токен
        var header = JwsHeader.with(MacAlgorithm.HS256).build();
        var token = jwtEncoder.encode(JwtEncoderParameters.from(header, claim)).getTokenValue();

        user.setPassword(null);

        return new TokenResponse(token, ttl.toSeconds(), UserMapper.toDto(user));
    }
}