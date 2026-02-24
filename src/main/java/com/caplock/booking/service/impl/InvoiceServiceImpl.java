package com.caplock.booking.service.impl;

import com.caplock.booking.entity.dto.InvoiceFormDto;
import com.caplock.booking.service.InvoiceService;
import com.caplock.booking.entity.dao.InvoiceEntity;
import com.caplock.booking.entity.dto.InvoiceDto;
import com.caplock.booking.repository.InvoiceRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public InvoiceDto update(Long id, InvoiceDto dto) {
        InvoiceEntity entity = modelMapper.map(dto, InvoiceEntity.class);
        entity.setId(id);
        InvoiceEntity saved = repository.save(entity);
        return modelMapper.map(saved, InvoiceDto.class);
    }

    @Transactional
    @Override
    public InvoiceDto generateInvoiceFromForm(InvoiceFormDto invoiceFormDto) {
        return create(new InvoiceDto(null,
                invoiceFormDto.bookingId(),
                invoiceFormDto.paymentId(),
                null,
                invoiceFormDto.holderName(),
                invoiceFormDto.holderEmail(),
                invoiceFormDto.totalAmount(),
                BigDecimal.ZERO,
                invoiceFormDto.totalAmount(),
                LocalDateTime.now(),
                null));
    }

    @Transactional
    @Override
    public InvoiceDto create(InvoiceDto dto) {
        // Ensure Hibernate treats this as a new entity
        dto.setId(null);
        InvoiceEntity saved = repository.save(modelMapper.map(dto, InvoiceEntity.class));
        if (saved != null) {
            log.info("Invoice created with id: {}", saved.getId());
            return generateInvoice(saved.getId());
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public InvoiceDto generateInvoice(long invoiceId) {
        var invoice = getById(invoiceId).orElseThrow(() -> new RuntimeException("Invoice not found"));
        try {
            if (invoice.getInvoiceNumber() == null)
                invoice.setInvoiceNumber(getInvoiceNumber());
            LocalDateTime now = LocalDateTime.now();

            String lineSeparator = System.lineSeparator();

            StringBuilder contentBuilder = new StringBuilder();

            contentBuilder.append("===== CAPLOCK BOOKING INVOICE =====")
                    .append(lineSeparator)
                    .append("Invoice Number: ").append(invoice.getInvoiceNumber())
                    .append(lineSeparator)
                    .append("Booking ID: ").append(invoice.getBookingId())
                    .append(lineSeparator)
                    .append("Amount: ").append(invoice.getTotalAmount())
                    .append(lineSeparator)
                    .append("Date: ").append(invoice.getIssuedAt())
                    .append(lineSeparator)
                    .append("====================================");

            String content = contentBuilder.toString();

            File dir = new File("invoices");
            if (!dir.exists()) {
                var ignore = dir.mkdirs();
            }

            String filePath = "invoices/" + invoice.getInvoiceNumber() + ".txt";
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(content);
            }

            InvoiceEntity entity = repository.findById(invoiceId)
                    .orElseThrow(() -> new RuntimeException("Invoice not found"));
            entity.setInvoiceNumber(invoice.getInvoiceNumber());
            entity.setIssuedAt(invoice.getIssuedAt());
            entity.setPdfUrl(filePath);

            InvoiceEntity saved = repository.save(entity);
            return modelMapper.map(saved, InvoiceDto.class);

        } catch (IOException e) {
            log.info("IOException in invoice generation: {}", e.getMessage());
            throw new RuntimeException("Invoice generation failed", e);
        }
    }

    @Nonnull
    private static String getInvoiceNumber() {
        return "INV-" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Override
    @Deprecated
    public Pair<Long, String> getNewInvoiceNumberAndId() {
        return Pair.with(getAll().stream().sorted().toList().getFirst().getId() + 1, getInvoiceNumber());
    }

    @Override
    public String getNewInvoiceNumber() {
        return getInvoiceNumber();
    }

}
