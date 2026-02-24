package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findByHolderName(String holderName);
}
