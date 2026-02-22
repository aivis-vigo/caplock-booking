package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.EventTicketConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventTicketConfigRepository extends JpaRepository<EventTicketConfigEntity, Long> {
    List<EventTicketConfigEntity> findAllByEventId(Long eventId);
}
