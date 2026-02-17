package com.caplock.booking.Service;

import com.caplock.booking.Mappers.CategoryMapper;
import com.caplock.booking.Mappers.InvoiceMapper;
import com.caplock.booking.Model.DTO.InvoiceDTO;
import com.caplock.booking.Repository.ICategoryRepository;
import com.caplock.booking.Repository.IInvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
