package com.caplock.booking.service;

import com.caplock.booking.mappers.InvoiceMapper;
import com.caplock.booking.entity.DAO.InvoiceDAO;
import com.caplock.booking.entity.DTO.InvoiceDTO;
import com.caplock.booking.repository.IInvoiceRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
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
            String invoiceNumber = "INV-" + UUID.randomUUID().toString().substring(0,8);
            LocalDateTime now = LocalDateTime.now();

            String content =
                    "===== CAPLOCK BOOKING INVOICE =====\n" +
                            "Invoice Number: " + invoiceNumber + "\n" +
                            "Booking ID: " + bookingId + "\n" +
                            "Amount: " + amount + "\n" +
                            "Date: " + now + "\n" +
                            "====================================";

            File dir = new File("invoices");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath = "invoices/" + invoiceNumber + ".txt";
            FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            writer.close();

            InvoiceDAO dao = new InvoiceDAO();
            dao.setBookingId(bookingId);
            dao.setAmount(amount);
            dao.setInvoiceNumber(invoiceNumber);
            dao.setCreatedAt(now);
            dao.setFilePath(filePath);

            return InvoiceMapper.toDTO(repository.save(dao));

        } catch (Exception e) {
            throw new RuntimeException("Invoice generation failed");
        }
    }

}
