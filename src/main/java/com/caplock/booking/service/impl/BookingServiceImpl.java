package com.caplock.booking.service.impl;

import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dao.BookingEntity;
import com.caplock.booking.repository.BookingRepository;
import com.caplock.booking.service.BookingService;
import com.caplock.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto create(BookingDto dto) {
        BookingEntity saved = bookingRepository.save(Mapper.toEntity(dto));
        return Mapper.toDto(saved);
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
    public BookingDto update(Long id, BookingDto dto) {
        BookingEntity entity = Mapper.toEntity(dto);
        entity.setId(id);
        return Mapper.toDto(bookingRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }
}
