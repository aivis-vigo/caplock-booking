package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
}
