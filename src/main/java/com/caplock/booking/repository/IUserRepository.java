package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
}
