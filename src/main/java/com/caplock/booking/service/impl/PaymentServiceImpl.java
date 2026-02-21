package com.caplock.booking.service.impl;

import com.caplock.booking.entity.dto.PaymentDto;
import com.caplock.booking.entity.dao.PaymentEntity;
import com.caplock.booking.repository.PaymentRepository;
import com.caplock.booking.service.PaymentService;
import com.caplock.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDto create(PaymentDto dto) {
        PaymentEntity saved = paymentRepository.save(Mapper.toEntity(dto));
        return Mapper.toDto(saved);
    }

    @Override
    public Optional<PaymentDto> getById(Long id) {
        return paymentRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public List<PaymentDto> getAll() {
        return paymentRepository.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    public PaymentDto update(Long id, PaymentDto dto) {
        PaymentEntity entity = Mapper.toEntity(dto);
        entity.setId(id);
        return Mapper.toDto(paymentRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }
}
