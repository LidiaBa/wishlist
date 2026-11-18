package com.example.wishlist.service;

import com.example.wishlist.domain.User;
import java.util.List;

public interface UserService {
    User create(User user);
    List<User> getAll();
    User get(Long id);
    User update(User user);
    void delete(Long id);
}
