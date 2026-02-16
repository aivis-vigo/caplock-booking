package com.caplock.booking.service;

import com.caplock.booking.entity.User;
import com.caplock.booking.repository.UserRepository;

public class UserService {
    UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id);
    }
}
