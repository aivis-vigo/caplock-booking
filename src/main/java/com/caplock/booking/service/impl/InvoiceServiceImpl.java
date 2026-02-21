package com.caplock.booking.service.impl;

import com.caplock.booking.entity.dto.InvoiceDto;
import com.caplock.booking.entity.dao.InvoiceEntity;
import com.caplock.booking.repository.InvoiceRepository;
import com.caplock.booking.service.InvoiceService;
import com.caplock.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Override
    public InvoiceDto create(InvoiceDto dto) {
        InvoiceEntity saved = invoiceRepository.save(Mapper.toEntity(dto));
        return Mapper.toDto(saved);
    }

    @Override
    public Optional<InvoiceDto> getById(Long id) {
        return invoiceRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public List<InvoiceDto> getAll() {
        return invoiceRepository.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    public InvoiceDto update(Long id, InvoiceDto dto) {
        InvoiceEntity entity = Mapper.toEntity(dto);
        entity.setId(id);
        return Mapper.toDto(invoiceRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        invoiceRepository.deleteById(id);
    }
}
