package com.caplock.booking.api.repository;

import com.caplock.booking.entity.dao.CategoryDAO;
import com.caplock.booking.repository.CategoryMockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMockRepositoryTest {

    private CategoryMockRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CategoryMockRepository();
    }

    @Test
    void save_newCategory_assignsIdAndStores() {
        CategoryDAO category = new CategoryDAO();
        category.setName("Electronics");

        CategoryDAO saved = repository.save(category);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getId());
    }

    @Test
    void save_categoryWithExistingId_doesNotOverrideId() {
        CategoryDAO category = new CategoryDAO();
        category.setId(99L);
        category.setName("Books");

        CategoryDAO saved = repository.save(category);

        assertEquals(99L, saved.getId());
    }

    @Test
    void save_multipleCategoriesWithoutId_incrementsIdSequentially() {
        CategoryDAO first = repository.save(new CategoryDAO());
        CategoryDAO second = repository.save(new CategoryDAO());

        assertEquals(1L, first.getId());
        assertEquals(2L, second.getId());
    }

    @Test
    void findById_existingId_returnsCategory() {
        CategoryDAO category = new CategoryDAO();
        category.setName("Sports");
        repository.save(category);

        Optional<CategoryDAO> found = repository.findById(1L);

        assertTrue(found.isPresent());
        assertEquals("Sports", found.get().getName());
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        Optional<CategoryDAO> found = repository.findById(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_emptyStorage_returnsEmptyList() {
        List<CategoryDAO> result = repository.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_afterSavingCategories_returnsAll() {
        repository.save(new CategoryDAO());
        repository.save(new CategoryDAO());

        List<CategoryDAO> result = repository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void deleteById_existingId_removesCategory() {
        CategoryDAO category = repository.save(new CategoryDAO());
        Long id = category.getId();

        repository.deleteById(id);

        assertTrue(repository.findById(id).isEmpty());
    }

    @Test
    void deleteById_nonExistingId_doesNotThrow() {
        assertDoesNotThrow(() -> repository.deleteById(999L));
    }

    @Test
    void save_updatesExistingCategory() {
        CategoryDAO category = new CategoryDAO();
        category.setName("Old Name");
        repository.save(category);

        category.setName("New Name");
        repository.save(category);

        assertEquals(1, repository.findAll().size());
        assertEquals("New Name", repository.findById(category.getId()).get().getName());
    }
}
