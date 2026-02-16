package com.caplock.booking.repository;

import com.caplock.booking.UserRole;
import com.caplock.booking.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    List<User> users = new ArrayList<>();

    public UserRepository() {
        users.add(new User(1, "John Doe", "john@example.com", "hashed_password", UserRole.USER, "token123", "2023-09-01", "2023-09-02"));
        users.add(new User(2, "Jane Smith", "jane@example.com", "hashed_password", UserRole.ADMIN, "token456", "2023-09-02", "2023-09-03"));
    }

    public User findById(Integer id) {
        return users.stream()
                .filter(user -> user.getId().equals((id)))
                .findFirst()
                .orElse(null);
    }
}
