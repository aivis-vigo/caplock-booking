package com.caplock.booking.repository;

import com.caplock.booking.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findById(Long id);

    List<Ticket> findByHolderName(String holderName);

}
