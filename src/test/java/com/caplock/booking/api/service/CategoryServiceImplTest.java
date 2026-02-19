package com.caplock.booking.api.service;

import com.caplock.booking.entity.dao.CategoryDAO;
import com.caplock.booking.entity.dto.CategoryDTO;
import com.caplock.booking.repository.ICategoryRepository;
import com.caplock.booking.service.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.verification.VerificationMode;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    private ICategoryRepository repository;
    private CategoryServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(ICategoryRepository.class);
        service = new CategoryServiceImpl(repository);
    }

    @Test
    void getAll_returnsListOfCategoryDTOs() {
        CategoryDAO dao1 = new CategoryDAO();
        dao1.setId(1L);
        dao1.setName("Electronics");

        CategoryDAO dao2 = new CategoryDAO();
        dao2.setId(2L);
        dao2.setName("Books");

        when(repository.findAll()).thenReturn(List.of(dao1, dao2));

        List<CategoryDTO> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
        assertEquals("Books", result.get(1).getName());
    }

    @Test
    void getAll_emptyRepository_returnsEmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<CategoryDTO> result = service.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getById_existingId_returnsCategoryDTO() {
        CategoryDAO dao = new CategoryDAO();
        dao.setId(1L);
        dao.setName("Sports");

        when(repository.findById(1L)).thenReturn(Optional.of(dao));

        CategoryDTO result = service.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Sports", result.getName());
    }

    @Test
    void getById_nonExistingId_throwsRuntimeException() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.getById(999L));

        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    void create_validDTO_savesCategoryAndReturnsDTO() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("Clothing");

        CategoryDAO savedDAO = new CategoryDAO();
        savedDAO.setId(1L);
        savedDAO.setName("Clothing");

        when(repository.save(any(CategoryDAO.class))).thenReturn(savedDAO);

        CategoryDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Clothing", result.getName());
        verify(repository, times(1)).save(any(CategoryDAO.class));
    }

    @Test
    void delete_existingId_callsRepositoryDeleteById() {
        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void delete_nonExistingId_doesNotThrow() {
        doNothing().when(repository).deleteById(999L);

        assertDoesNotThrow(() -> service.delete(999L));
        verify(repository, times(1)).deleteById(999L);
    }
}
