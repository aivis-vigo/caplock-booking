package com.caplock.booking.service;

import com.caplock.booking.util.InvoiceMapper;
import com.caplock.booking.entity.dao.InvoiceDAO;
import com.caplock.booking.entity.dto.InvoiceDTO;
import com.caplock.booking.repository.IInvoiceRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements IInvoiceService{
    private final IInvoiceRepository repository;

    public InvoiceServiceImpl(IInvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<InvoiceDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(InvoiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO getById(Long id) {
        return repository.findById(id)
                .map(InvoiceMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }


    @Override
    public InvoiceDTO create(InvoiceDTO dto) {
        return InvoiceMapper.toDTO(
                repository.save(InvoiceMapper.toDAO(dto))
        );
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public InvoiceDTO genereteInvoice(Long bookingId, BigDecimal amount) {
        try {
            String invoiceNumber = "INV-" + UUID.randomUUID().toString().substring(0, 8);
            LocalDateTime now = LocalDateTime.now();

            String lineSeparator = System.getProperty("line.separator");

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
                dir.mkdirs();
            }

            String filePath = "invoices/" + invoiceNumber + ".txt";
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(content);
            }

            InvoiceDAO dao = new InvoiceDAO();
            dao.setBookingId(bookingId);
            dao.setAmount(amount);
            dao.setInvoiceNumber(invoiceNumber);
            dao.setCreatedAt(now);
            dao.setFilePath(filePath);

            return InvoiceMapper.toDTO(repository.save(dao));

        } catch (IOException e) {
            throw new RuntimeException("Invoice generation failed", e);
        }
    }

}
