package com.caplock.booking.repository;

import com.caplock.booking.UserRole;
import com.caplock.booking.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final List<User> users = new ArrayList<>();

    static {
        users.add(new User(1, "John Doe", "john@example.com", "hashed_password", UserRole.USER, "token123", "2023-09-01", "2023-09-02"));
        users.add(new User(2, "Jane Smith", "jane@example.com", "hashed_password", UserRole.ADMIN, "token456", "2023-09-02", "2023-09-03"));
    }

    public User findById(Integer id) {
        return users.stream()
                .filter(user -> user.getId().equals((id)))
                .findFirst()
                .orElse(null);
    }

    public List<User> findAll() {
        return users.stream().toList();
    }

    public void save(User user) {
        users.add(user);
    }

    public void update(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                return;
            }
        }
    }

    public void deleteById(Integer id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}
