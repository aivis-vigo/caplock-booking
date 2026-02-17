package com.caplock.booking.Service;

import com.caplock.booking.Mappers.CategoryMapper;
import com.caplock.booking.Mappers.InvoiceMapper;
import com.caplock.booking.Mappers.PaymentMapper;
import com.caplock.booking.Model.DTO.CategoryDTO;
import com.caplock.booking.Model.DTO.InvoiceDTO;
import com.caplock.booking.Model.DTO.PaymentDTO;
import com.caplock.booking.Repository.ICategoryRepository;
import com.caplock.booking.Repository.IPaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements IPaymentService {
    private final IPaymentRepository repository;

    public PaymentServiceImpl(IPaymentRepository repository) {
        this.repository = repository;
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
        return PaymentMapper.toDTO(
                repository.save(PaymentMapper.toDAO(dto))
        );
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
