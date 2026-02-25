package com.caplock.booking.service.impl;

import com.caplock.booking.config.ModelMapperConfig;
import com.caplock.booking.entity.StatusBookingEnum;
import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dao.BookingEntity;
import com.caplock.booking.entity.dto.BookingItemDto;
import com.caplock.booking.entity.dto.EventDto;
import com.caplock.booking.repository.BookingRepository;
import com.caplock.booking.service.*;
import com.caplock.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Triplet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ModelMapperConfig modelMapper;


    public BookingDto createNewBooking(BookingDto bookingDTO) {
        log.info("Creating new booking for customerId={}", bookingDTO.getUserId());

        BookingEntity booking = modelMapper.modelMapper().map(bookingDTO, BookingEntity.class);

        booking.setStatus(StatusBookingEnum.WAITING_PAYMENT);
        booking.setDiscountCode(bookingDTO.getDiscountCode());
        booking.setConfirmationCode(UUID.randomUUID().toString());

        BookingEntity savedBooking = bookingRepository.save(booking);

        log.info("Booking created successfully — id={}", booking.getId());

        return modelMapper.modelMapper().map(savedBooking, BookingDto.class);
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
        bookingRepository.delete(entity);
        BookingEntity saved = bookingRepository.saveAndFlush(Mapper.toEntity(dto));
        return Mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }
}
