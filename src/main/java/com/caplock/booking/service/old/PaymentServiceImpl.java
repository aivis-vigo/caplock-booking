package com.caplock.booking.service.old;

import com.caplock.booking.util.old.PaymentMapper;
import com.caplock.booking.entity.old.dao.PaymentDAO;
import com.caplock.booking.entity.old.dto.PaymentDTO;
import com.caplock.booking.repository.old.jpa.IPaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("legacyPaymentServiceImpl")
public class PaymentServiceImpl implements IPaymentService {
    private final IPaymentRepository repository;
    private final InvoiceServiceImpl invoiceServiceImpl;

    public PaymentServiceImpl(IPaymentRepository repository, InvoiceServiceImpl invoiceServiceImpl) {
        this.repository = repository;
        this.invoiceServiceImpl = invoiceServiceImpl;
    }

    @Override
    public List<PaymentDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(PaymentMapper:: toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO getById(Long id) {
        return repository.findById(id)
                .map(PaymentMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public PaymentDTO create(PaymentDTO dto) {
        PaymentDAO dao = new PaymentDAO();
        if("PAID".equalsIgnoreCase(dto.getStatus())){
            invoiceServiceImpl.genereteInvoice(dto.getBookingId(),dto.getAmount());
        }
        return PaymentMapper.toDTO(repository.save(dao));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
