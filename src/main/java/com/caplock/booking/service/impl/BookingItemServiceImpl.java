package com.caplock.booking.service.impl;

import com.caplock.booking.entity.dto.BookingItemDto;
import com.caplock.booking.entity.dao.BookingItemEntity;
import com.caplock.booking.repository.BookingItemRepository;
import com.caplock.booking.service.BookingItemService;
import com.caplock.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingItemServiceImpl implements BookingItemService {
    private final BookingItemRepository bookingItemRepository;

    @Override
    public BookingItemDto create(BookingItemDto dto) {
        BookingItemEntity saved = bookingItemRepository.save(Mapper.toEntity(dto));
        return Mapper.toDto(saved);
    }

    @Override
    public Optional<BookingItemDto> getById(Long id) {
        return bookingItemRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public List<BookingItemDto> getAll() {
        return bookingItemRepository.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    public List<BookingItemDto> getAllByBookingId(long bookingId) {
        return bookingItemRepository.findByBookingId(bookingId)
                .stream()
                .map(Mapper::toDto)
                .toList();
    }

    @Override
    public BookingItemDto update(Long id, BookingItemDto dto) {
        BookingItemEntity entity = Mapper.toEntity(dto);
        entity.setId(id);
        return Mapper.toDto(bookingItemRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        bookingItemRepository.deleteById(id);
    }
}
