package com.caplock.booking.Service;

import com.caplock.booking.Mappers.CategoryMapper;
import com.caplock.booking.Model.DTO.CategoryDTO;
import com.caplock.booking.Repository.ICategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {
    private final ICategoryRepository repository;

    public CategoryServiceImpl(ICategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CategoryDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getById(Long id) {
        return repository.findById(id)
                .map(CategoryMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public CategoryDTO create(CategoryDTO dto) {
        return CategoryMapper.toDTO(
                repository.save(CategoryMapper.toDAO(dto))
        );
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
