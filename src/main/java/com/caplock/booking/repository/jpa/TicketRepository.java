package com.caplock.booking.repository.jpa;

import com.caplock.booking.entity.object.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAll();

    List<Ticket> findByHolderName(String holderName);

}
