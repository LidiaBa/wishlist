package com.example.wishlist.service;

import com.example.wishlist.domain.User;
import com.example.wishlist.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (user.getLogin() == null || user.getLogin().isBlank()) throw new IllegalArgumentException("Login is required");
        if (user.getPassword() == null || user.getPassword().isBlank()) throw new IllegalArgumentException("Password is required");
        if (user.getName() == null || user.getName().isBlank()) throw new IllegalArgumentException("Name is required");

        User existing = userRepository.findByLogin(user.getLogin()).orElse(null);
        if (existing != null) {
            throw new IllegalStateException("User with login '" + user.getLogin() + "' already exists");
        }

        return userRepository.save(user); // save() заменяет create()
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        users.forEach(u -> u.setPassword(null)); // Скрываем пароли
        return users;
    }

    @Override
    public User get(Long id) {
        if (id == null) throw new IllegalArgumentException("User ID cannot be null");

        User user = userRepository.findById(id).orElse(null);
        if (user == null) throw new RuntimeException("User not found with id: " + id);

        user.setPassword(null);
        return user;
    }

    @Override
    public User update(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (user.getId() == null) throw new IllegalArgumentException("User ID is required for update");

        User existing = userRepository.findById(user.getId()).orElse(null);
        if (existing == null) throw new RuntimeException("User not found with id: " + user.getId());
        if (user.getName() != null && user.getName().isBlank()) throw new IllegalArgumentException("Name cannot be empty");

        user.setLogin(existing.getLogin()); // Логин менять нельзя
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(existing.getPassword());
        }
        User updated = userRepository.save(user); // save() заменяет update()
        updated.setPassword(null);
        return updated;
    }

    @Override
    public void delete(Long id) {
        if (id == null) throw new IllegalArgumentException("User ID cannot be null");
        User existing = userRepository.findById(id).orElse(null);
        if (existing == null) throw new RuntimeException("User not found with id: " + id);

        userRepository.deleteById(id); // deleteById() заменяет delete()
    }
}
