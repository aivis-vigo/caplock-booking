package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
