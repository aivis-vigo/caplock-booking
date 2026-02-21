package com.caplock.booking.service.old;

import com.caplock.booking.entity.old.dto.InvoiceDTO;

import java.math.BigDecimal;
import java.util.List;

public interface IInvoiceService {
    List<InvoiceDTO> getAll();
    InvoiceDTO getById(Long id);
    InvoiceDTO create(InvoiceDTO dto);
    void delete(Long id);
    InvoiceDTO genereteInvoice(Long bookingId, BigDecimal amount );
}
