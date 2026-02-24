package com.caplock.booking.service;

import com.caplock.booking.entity.dto.InvoiceDto;
import com.caplock.booking.entity.dto.InvoiceFormDto;
import org.javatuples.Pair;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    InvoiceDto generateInvoiceFromForm(InvoiceFormDto invoiceFormDto);

    InvoiceDto create(InvoiceDto dto);

    Optional<InvoiceDto> getById(Long id);

    List<InvoiceDto> getAll();

    InvoiceDto update(Long id, InvoiceDto dto);

    void delete(Long id);

    InvoiceDto generateInvoice(long invoiceId);

    Pair<Long, String> getNewInvoiceNumberAndId();

    public String getNewInvoiceNumber();
}
