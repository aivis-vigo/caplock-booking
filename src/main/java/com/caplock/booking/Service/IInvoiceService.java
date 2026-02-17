package com.caplock.booking.Service;

import com.caplock.booking.Model.DTO.InvoiceDTO;

import java.util.List;

public interface IInvoiceService {
    List<InvoiceDTO> getAll();
    InvoiceDTO getById(Long id);
    InvoiceDTO create(InvoiceDTO dto);
    void delete(Long id);
}
