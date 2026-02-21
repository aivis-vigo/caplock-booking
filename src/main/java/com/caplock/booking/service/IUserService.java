package com.caplock.booking.service;

import com.caplock.booking.entity.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    UserDto create(UserDto dto);

    Optional<UserDto> getById(Long id);

    List<UserDto> getAll();

    UserDto update(Long id, UserDto dto);

    void delete(Long id);
}
