package com.example.wishlist.controller;

import com.example.wishlist.domain.User;
import com.example.wishlist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)// чтоб статус 201
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    // Аналог doPut
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @RequestAttribute("userId") Long currentUserId,
            @RequestAttribute("userRole") String userRole) {

        if (!"ADMIN".equals(userRole) && !id.equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
