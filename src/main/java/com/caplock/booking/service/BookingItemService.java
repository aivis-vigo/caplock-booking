package com.caplock.booking.service;

import com.caplock.booking.entity.dto.BookingItemDto;

import java.util.List;
import java.util.Optional;

public interface BookingItemService {
    BookingItemDto create(BookingItemDto dto);

    Optional<BookingItemDto> getById(Long id);

    List<BookingItemDto> getAll();

    List<BookingItemDto> getAllByBookingId(long bookingId);

    BookingItemDto update(Long id, BookingItemDto dto);

    void delete(Long id);
}
