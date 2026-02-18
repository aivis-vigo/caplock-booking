package com.caplock.booking.service;

import com.caplock.booking.dto.UserCreationDTO;
import com.caplock.booking.dto.UserDTO;
import com.caplock.booking.dto.UserDTOMapper;
import com.caplock.booking.entity.User;
import com.caplock.booking.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

public class UserService {
    UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    public UserService(UserDTOMapper userDTOMapper) {
        this.userRepository = new UserRepository();
        this.userDTOMapper = userDTOMapper;
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userDTOMapper)
                .toList();
    }

    public void saveUser(UserCreationDTO user) {
        User newUser = new User(
                3,
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                "12345",
                LocalDate.now().toString(),
                LocalDate.now().toString()
        );
        userRepository.save(newUser);
    }

    public void updateUser(Integer id, UserCreationDTO user) {
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setName(user.getName());
        updateUser.setEmailHash(user.getEmail());
        updateUser.setPasswordHash(user.getPassword());
        updateUser.setRole(user.getRole());
        userRepository.update(updateUser);
    }

    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }
}
