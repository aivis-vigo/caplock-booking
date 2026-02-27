package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findByHolderName(String holderName);

    @Query("SELECT t FROM TicketEntity t WHERE t.bookingId = :bookingId")
    List<TicketEntity> findByBookingId(@Param("bookingId") Long bookingId);
}
