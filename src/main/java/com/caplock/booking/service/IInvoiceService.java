package com.caplock.booking.service;

import com.caplock.booking.entity.DTO.InvoiceDTO;

import java.math.BigDecimal;
import java.util.List;

public interface IInvoiceService {
    List<InvoiceDTO> getAll();
    InvoiceDTO getById(Long id);
    InvoiceDTO create(InvoiceDTO dto);
    void delete(Long id);
    InvoiceDTO genereteInvoice(Long bookingId, BigDecimal amount );
}
