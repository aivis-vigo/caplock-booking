package com.caplock.booking.service;

import com.caplock.booking.entity.dto.InvoiceDto;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    InvoiceDto create(InvoiceDto dto);

    Optional<InvoiceDto> getById(Long id);

    List<InvoiceDto> getAll();

    InvoiceDto update(Long id, InvoiceDto dto);

    void delete(Long id);
}
