package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.BookingItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingItemRepository extends JpaRepository<BookingItemEntity, Long> {
    List<BookingItemEntity> findByBookingId(long bookingId);
}
