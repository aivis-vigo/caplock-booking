package com.caplock.booking.service;

import com.caplock.booking.entity.dao.UserEntity;
import com.caplock.booking.entity.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface IUserService extends UserDetailsService {
    UserDto create(UserDto dto);

    Optional<UserDto> getById(Long id);

    long getUserIdByEmail(String email);

    List<UserDto> getAll();

    UserDto update(Long id, UserDto dto);

    void delete(Long id);

    Optional<UserDto> findByEmailHash(String email);

    boolean existsByEmailHash(String email);
}
