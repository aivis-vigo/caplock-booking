package com.caplock.booking.api.service;

import com.caplock.booking.entity.dao.InvoiceEntity;
import com.caplock.booking.entity.dto.InvoiceDto;
import com.caplock.booking.repository.InvoiceRepository;
import com.caplock.booking.service.InvoiceService;
import com.caplock.booking.service.impl.InvoiceServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

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

    private ModelMapper mapper;
    private InvoiceRepository repository;
    private InvoiceService service;

    @BeforeEach
    void setUp() {
        mapper = mock(ModelMapper.class);
        repository = mock(InvoiceRepository.class);

        service = new InvoiceServiceImpl(repository, mapper);
    }

    // ───── getAll ─────

    @Test
    @Deprecated
    @Disabled
    void getAll_returnsListOfInvoiceDTOs() {
        InvoiceEntity dao1 = new InvoiceEntity();
        dao1.setId(1L);
        dao1.setTotalAmount(new BigDecimal("100.00"));

        InvoiceEntity dao2 = new InvoiceEntity();
        dao2.setId(2L);
        dao2.setTotalAmount(new BigDecimal("200.00"));

        when(repository.findAll()).thenReturn(List.of(dao1, dao2));

        List<InvoiceDto> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals(0, new BigDecimal("100.00").compareTo(result.get(0).getTotalAmount()));
        assertEquals(0, new BigDecimal("200.00").compareTo(result.get(1).getTotalAmount()));
    }

    @Test
    void getAll_emptyRepository_returnsEmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<InvoiceDto> result = service.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ───── getById ─────

    @Test
    @Deprecated
    @Disabled
    void getById_existingId_returnsInvoiceDto() {
        InvoiceEntity dao = new InvoiceEntity();
        dao.setId(1L);
        dao.setTotalAmount(new BigDecimal("300.00"));

        when(repository.findById(1L)).thenReturn(Optional.of(dao));

        InvoiceDto result = service.getById(1L).orElse(null);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(0, new BigDecimal("300.00").compareTo(result.getTotalAmount()));
    }

    @Test
    @Deprecated
    @Disabled
    void getById_nonExistingId_throwsRuntimeException() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.getById(999L));

        assertEquals("Category not found", exception.getMessage());
    }

    // ───── create ─────

    @Test
    @Deprecated
    @Disabled
    void create_validDTO_savesInvoiceAndReturnsDTO() {
        InvoiceDto dto = new InvoiceDto();
        dto.setTotalAmount(new BigDecimal("500.00"));

        InvoiceEntity savedDAO = new InvoiceEntity();
        savedDAO.setId(1L);
        savedDAO.setTotalAmount(new BigDecimal("500.00"));

        when(repository.save(any(InvoiceEntity.class))).thenReturn(savedDAO);

        InvoiceDto result = service.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(0, new BigDecimal("500.00").compareTo(result.getTotalAmount()));
        verify(repository, times(1)).save(any(InvoiceEntity.class));
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
    @Deprecated
    @Disabled
    void generateInvoice_validInput_createsFileAndSavesDAO() {
        Long bookingId = 1L;
        BigDecimal amount = new BigDecimal("750.00");

        when(repository.save(any(InvoiceEntity.class))).thenAnswer(invocation -> {
            InvoiceEntity dao = invocation.getArgument(0);
            dao.setId(1L);
            return dao;
        });

        InvoiceDto result = service.generateInvoice(1L);;

        assertNotNull(result);
        assertEquals(0, amount.compareTo(result.getTotalAmount()));
        assertNotNull(result.getInvoiceNumber());
        assertTrue(result.getInvoiceNumber().startsWith("INV-"));
        verify(repository, times(1)).save(any(InvoiceEntity.class));
    }

    @Test
    @Deprecated
    @Disabled
    void generateInvoice_savesDAOWithCorrectFields() {
        Long bookingId = 5L;
        BigDecimal amount = new BigDecimal("1200.00");

        when(repository.save(any(InvoiceEntity.class))).thenAnswer(invocation -> {
            InvoiceEntity dao = invocation.getArgument(0);
            dao.setId(1L);
            return dao;
        });

        InvoiceDto result = service.generateInvoice(1L);

        // Check that the file was actually created
        verify(repository).save(argThat(dao ->
                dao.getBookingId().equals(bookingId) &&
                        dao.getTotalAmount().compareTo(amount) == 0 &&
                        dao.getInvoiceNumber().startsWith("INV-") &&
                        dao.getPdfUrl().startsWith("invoices/INV-") &&
                        dao.getCreatedAt() != null
        ));
    }

    @Test
    @Deprecated
    @Disabled
    void generateInvoice_physicalFileIsCreated() {
        Long bookingId = 2L;
        BigDecimal amount = new BigDecimal("400.00");

        when(repository.save(any(InvoiceEntity.class))).thenAnswer(invocation -> {
            InvoiceEntity dao = invocation.getArgument(0);
            dao.setId(1L);
            return dao;
        });

        service.generateInvoice(1L);

        // Check that the invoices directory and file have been created
        File dir = new File("invoices");
        assertTrue(dir.exists() && dir.isDirectory());

        File[] files = dir.listFiles((d, name) -> name.startsWith("INV-") && name.endsWith(".txt"));
        assertNotNull(files);
        assertTrue(files.length > 0);
    }

    @AfterEach
    void cleanUp() throws IOException {
        File dir = new File("invoices");
        if (dir.exists()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                file.delete();
            }
            dir.delete();
        }
    }
}
