package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.EventTicketConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTicketConfigRepository extends JpaRepository<EventTicketConfigEntity, Long> {
}
