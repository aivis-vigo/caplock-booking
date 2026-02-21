package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.BookingItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingItemRepository extends JpaRepository<BookingItemEntity, Long> {
}
