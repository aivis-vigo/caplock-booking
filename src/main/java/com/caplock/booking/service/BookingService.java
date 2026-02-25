package com.caplock.booking.service;

import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dto.BookingItemDto;
import org.javatuples.Triplet;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    BookingDto createNewBooking(BookingDto newBooking);

    Optional<BookingDto> getById(Long id);

    List<BookingDto> getAll();

    BookingDto update(Long id, BookingDto dto);

    void delete(Long id);
}
