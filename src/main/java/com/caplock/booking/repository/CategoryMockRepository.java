package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.CategoryDAO;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CategoryMockRepository implements ICategoryRepository {

    private final Map<Long, CategoryDAO> storage = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public List<CategoryDAO> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<CategoryDAO> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public CategoryDAO save(CategoryDAO category) {
        if (category.getId() == null) {
            category.setId(idCounter++);
        }
        storage.put(category.getId(), category);
        return category;
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}