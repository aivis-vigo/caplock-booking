package com.caplock.booking.api.service;

import com.caplock.booking.entity.dao.InvoiceDAO;
import com.caplock.booking.entity.dto.InvoiceDTO;
import com.caplock.booking.repository.IInvoiceRepository;
import com.caplock.booking.service.InvoiceServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InvoiceServiceImplTest {

    private IInvoiceRepository repository;
    private InvoiceServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(IInvoiceRepository.class);
        service = new InvoiceServiceImpl(repository);
    }

    // ───── getAll ─────

    @Test
    void getAll_returnsListOfInvoiceDTOs() {
        InvoiceDAO dao1 = new InvoiceDAO();
        dao1.setId(1L);
        dao1.setAmount(new BigDecimal("100.00"));

        InvoiceDAO dao2 = new InvoiceDAO();
        dao2.setId(2L);
        dao2.setAmount(new BigDecimal("200.00"));

        when(repository.findAll()).thenReturn(List.of(dao1, dao2));

        List<InvoiceDTO> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals(0, new BigDecimal("100.00").compareTo(result.get(0).getAmount()));
        assertEquals(0, new BigDecimal("200.00").compareTo(result.get(1).getAmount()));
    }

    @Test
    void getAll_emptyRepository_returnsEmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<InvoiceDTO> result = service.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ───── getById ─────

    @Test
    void getById_existingId_returnsInvoiceDTO() {
        InvoiceDAO dao = new InvoiceDAO();
        dao.setId(1L);
        dao.setAmount(new BigDecimal("300.00"));

        when(repository.findById(1L)).thenReturn(Optional.of(dao));

        InvoiceDTO result = service.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(0, new BigDecimal("300.00").compareTo(result.getAmount()));
    }

    @Test
    void getById_nonExistingId_throwsRuntimeException() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.getById(999L));

        assertEquals("Category not found", exception.getMessage());
    }

    // ───── create ─────

    @Test
    void create_validDTO_savesInvoiceAndReturnsDTO() {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setAmount(new BigDecimal("500.00"));

        InvoiceDAO savedDAO = new InvoiceDAO();
        savedDAO.setId(1L);
        savedDAO.setAmount(new BigDecimal("500.00"));

        when(repository.save(any(InvoiceDAO.class))).thenReturn(savedDAO);

        InvoiceDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(0, new BigDecimal("500.00").compareTo(result.getAmount()));
        verify(repository, times(1)).save(any(InvoiceDAO.class));
    }

    // ───── delete ─────

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

    // ───── generateInvoice ─────

    @Test
    void generateInvoice_validInput_createsFileAndSavesDAO() {
        Long bookingId = 1L;
        BigDecimal amount = new BigDecimal("750.00");

        when(repository.save(any(InvoiceDAO.class))).thenAnswer(invocation -> {
            InvoiceDAO dao = invocation.getArgument(0);
            dao.setId(1L);
            return dao;
        });

        InvoiceDTO result = service.genereteInvoice(bookingId, amount);

        assertNotNull(result);
        assertEquals(0, amount.compareTo(result.getAmount()));
        assertNotNull(result.getInvoiceNumber());
        assertTrue(result.getInvoiceNumber().startsWith("INV-"));
        verify(repository, times(1)).save(any(InvoiceDAO.class));
    }

    @Test
    void generateInvoice_savesDAOWithCorrectFields() {
        Long bookingId = 5L;
        BigDecimal amount = new BigDecimal("1200.00");

        when(repository.save(any(InvoiceDAO.class))).thenAnswer(invocation -> {
            InvoiceDAO dao = invocation.getArgument(0);
            dao.setId(1L);
            return dao;
        });

        InvoiceDTO result = service.genereteInvoice(bookingId, amount);

        // Check that the file was actually created
        verify(repository).save(argThat(dao ->
                dao.getBookingId().equals(bookingId) &&
                        dao.getAmount().compareTo(amount) == 0 &&
                        dao.getInvoiceNumber().startsWith("INV-") &&
                        dao.getFilePath().startsWith("invoices/INV-") &&
                        dao.getCreatedAt() != null
        ));
    }

    @Test
    void generateInvoice_physicalFileIsCreated() {
        Long bookingId = 2L;
        BigDecimal amount = new BigDecimal("400.00");

        when(repository.save(any(InvoiceDAO.class))).thenAnswer(invocation -> {
            InvoiceDAO dao = invocation.getArgument(0);
            dao.setId(1L);
            return dao;
        });

        service.genereteInvoice(bookingId, amount);

        // Check that the invoices directory and file have been created
        File dir = new File("invoices");
        assertTrue(dir.exists() && dir.isDirectory());

        File[] files = dir.listFiles((d, name) -> name.startsWith("INV-") && name.endsWith(".txt"));
        assertNotNull(files);
        assertTrue(files.length > 0);
    }

    @AfterEach
    void cleanUp() throws IOException {
        // Удаляем созданные тестовые файлы
        File dir = new File("invoices");
        if (dir.exists()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                file.delete();
            }
            dir.delete();
        }
    }
}
