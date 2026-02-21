package com.caplock.booking.service;

import com.caplock.booking.entity.dto.CreateTicketDTO;
import com.caplock.booking.util.PaymentMapper;
import com.caplock.booking.entity.dao.PaymentDAO;
import com.caplock.booking.entity.dto.PaymentDTO;
import com.caplock.booking.repository.IPaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements IPaymentService {
    private final IPaymentRepository repository;
    private final IInvoiceService invoiceService;
    private final TicketService ticketService;

    public PaymentServiceImpl(IPaymentRepository repository, IInvoiceService invoiceService, TicketService ticketService) {
        this.repository = repository;
        this.invoiceService = invoiceService;
        this.ticketService = ticketService;
    }

    @Override
    public List<PaymentDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(PaymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO getById(Long id) {
        return repository.findById(id)
                .map(PaymentMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Override
    public PaymentDTO create(PaymentDTO dto) {
        PaymentDAO dao = PaymentMapper.toDAO(dto);
        PaymentDAO saved = repository.save(dao);

        if ("PAID".equalsIgnoreCase(dto.getStatus())) {
            CreateTicketDTO ticketDTO = new CreateTicketDTO();
            ticketDTO.setBookingId(dto.getBookingId());
            ticketService.create(ticketDTO);

            invoiceService.genereteInvoice(dto.getBookingId(), dto.getAmount());
        }

        return PaymentMapper.toDTO(saved);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
