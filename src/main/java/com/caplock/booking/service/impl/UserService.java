package com.caplock.booking.service.impl;

import com.caplock.booking.entity.dao.UserEntity;
import com.caplock.booking.entity.dto.UserDto;
import com.caplock.booking.repository.IUserRepository;
import com.caplock.booking.service.IUserService;
import com.caplock.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository userRepository;

    @Override
    public UserDto create(UserDto dto) {
        UserEntity saved = userRepository.save(Mapper.toEntity(dto));
        return Mapper.toDto(saved);
    }

    @Override
    public Optional<UserDto> getById(Long id) {
        return userRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        UserEntity entity = Mapper.toEntity(dto);
        entity.setId(id);
        return Mapper.toDto(userRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
