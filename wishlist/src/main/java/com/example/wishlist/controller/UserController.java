package com.example.wishlist.controller;

import com.example.wishlist.domain.User;
import com.example.wishlist.dto.UserDto;
import com.example.wishlist.mapper.UserMapper;
import com.example.wishlist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAll().stream().map(UserMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return UserMapper.toDto(userService.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody User user) {
        return UserMapper.toDto(userService.create(user));
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        Long currentUserId = Long.valueOf(Objects.requireNonNull(jwt.getSubject(), "ID пользователя отсутствует в токене"));
        String userRole = jwt.getClaimAsString("roles");

        if (!"ADMIN".equals(userRole) && !id.equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
