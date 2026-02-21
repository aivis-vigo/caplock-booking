package com.caplock.booking.service;

import com.caplock.booking.entity.dto.BookingDto;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    BookingDto create(BookingDto dto);

    Optional<BookingDto> getById(Long id);

    List<BookingDto> getAll();

    BookingDto update(Long id, BookingDto dto);

    void delete(Long id);
}
