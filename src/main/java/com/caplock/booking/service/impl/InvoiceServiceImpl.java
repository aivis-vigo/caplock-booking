package com.caplock.booking.service.impl;

import com.caplock.booking.service.InvoiceService;
import com.caplock.booking.entity.dao.InvoiceEntity;
import com.caplock.booking.entity.dto.InvoiceDto;
import com.caplock.booking.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public List<InvoiceDto> getAll() {
        return repository.findAll()
                .stream()
                .map(dao -> modelMapper.map(dao, InvoiceDto.class))
                .toList();
    }

    @Override
    public Optional<InvoiceDto> getById(Long id) {
        return repository.findById(id)
                .map(dao -> modelMapper.map(dao, InvoiceDto.class));
    }

    @Override
    public InvoiceDto update(Long id, InvoiceDto dto) {
        InvoiceEntity entity = modelMapper.map(dto, InvoiceEntity.class);
        entity.setId(id);
        InvoiceEntity saved = repository.save(entity);
        return modelMapper.map(saved, InvoiceDto.class);
    }

    @Override
    public InvoiceDto create(InvoiceDto dto) {
        InvoiceEntity saved = repository.save(modelMapper.map(dto, InvoiceEntity.class));
        return modelMapper.map(saved, InvoiceDto.class);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public InvoiceDto generateInvoice(Long bookingId, BigDecimal amount) {
        try {
            String invoiceNumber = "INV-" + UUID.randomUUID().toString().substring(0, 8);
            LocalDateTime now = LocalDateTime.now();

            String lineSeparator = System.lineSeparator();

            StringBuilder contentBuilder = new StringBuilder();

            contentBuilder.append("===== CAPLOCK BOOKING INVOICE =====")
                    .append(lineSeparator)
                    .append("Invoice Number: ").append(invoiceNumber)
                    .append(lineSeparator)
                    .append("Booking ID: ").append(bookingId)
                    .append(lineSeparator)
                    .append("Amount: ").append(amount)
                    .append(lineSeparator)
                    .append("Date: ").append(now)
                    .append(lineSeparator)
                    .append("====================================");

            String content = contentBuilder.toString();

            File dir = new File("invoices");
            if (!dir.exists()) {
                var ignore = dir.mkdirs();
            }

            String filePath = "invoices/" + invoiceNumber + ".txt";
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(content);
            }

            InvoiceEntity dao = new InvoiceEntity();
            dao.setBookingId(bookingId);
            dao.setTotalAmount(amount);
            dao.setInvoiceNumber(invoiceNumber);
            dao.setIssuedAt(now);
            dao.setPdfUrl(filePath);

            InvoiceEntity saved = repository.save(dao);
            return modelMapper.map(saved, InvoiceDto.class);

        } catch (IOException e) {
            throw new RuntimeException("Invoice generation failed", e);
        }
    }

}
