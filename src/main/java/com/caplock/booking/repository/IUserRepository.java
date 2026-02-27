package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailHash(String email);

    boolean existsByEmailHash(String email);
}
