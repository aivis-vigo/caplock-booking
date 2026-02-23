package com.caplock.booking.service.impl;

import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dao.BookingEntity;
import com.caplock.booking.entity.dto.BookingItemDto;
import com.caplock.booking.entity.dto.EventDto;
import com.caplock.booking.repository.BookingRepository;
import com.caplock.booking.service.*;
import com.caplock.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.javatuples.Triplet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingItemService bookingItemService;

    @Transactional
    @Override
    public Triplet<Optional<BookingDto>, Boolean, String> create(BookingDto dto, List<BookingItemDto> items) {
        if (dto.getId() != null && bookingRepository.existsById(dto.getId()))
            return Triplet.with(Optional.empty(), false, "Booking already exists");

        BookingEntity entity = Mapper.toEntity(dto);
        entity.setId(null);
        BookingEntity saved = bookingRepository.save(entity);

        for (BookingItemDto item : items) {
            item.setBookingId(saved.getId());
            bookingItemService.create(item);
        }

        return Triplet.with(Optional.of(Mapper.toDto(saved)), true, "OK");
    }

    @Override
    public Optional<BookingDto> getById(Long id) {
        return bookingRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public List<BookingDto> getAll() {
        return bookingRepository.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    @Transactional
    public BookingDto update(Long id, BookingDto dto) {
        BookingEntity entity = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        BookingEntity saved = bookingRepository.saveAndFlush(Mapper.toEntity(dto));
        return Mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }
}
