package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
}
