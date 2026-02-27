package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.EventEntity;
import com.caplock.booking.entity.dao.EventSeatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventSeatRepository extends JpaRepository<EventSeatsEntity, Long> {
}
