package com.caplock.booking.api.service;

import com.caplock.booking.entity.dao.PaymentDAO;
import com.caplock.booking.entity.dto.PaymentDTO;
import com.caplock.booking.repository.IPaymentRepository;
import com.caplock.booking.service.InvoiceServiceImpl;
import com.caplock.booking.service.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    private IPaymentRepository repository;
    private InvoiceServiceImpl invoiceServiceImpl;
    private PaymentServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(IPaymentRepository.class);
        invoiceServiceImpl = mock(InvoiceServiceImpl.class);
        service = new PaymentServiceImpl(repository, invoiceServiceImpl);
    }

    // ───── getAll ─────

    @Test
    void getAll_returnsListOfPaymentDTOs() {
        PaymentDAO dao1 = new PaymentDAO();
        dao1.setId(1L);
        dao1.setAmount(new BigDecimal("100.00"));

        PaymentDAO dao2 = new PaymentDAO();
        dao2.setId(2L);
        dao2.setAmount(new BigDecimal("200.00"));

        when(repository.findAll()).thenReturn(List.of(dao1, dao2));

        List<PaymentDTO> result = service.getAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAll_emptyRepository_returnsEmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<PaymentDTO> result = service.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ───── getById ─────

    @Test
    void getById_existingId_returnsPaymentDTO() {
        PaymentDAO dao = new PaymentDAO();
        dao.setId(1L);
        dao.setAmount(new BigDecimal("300.00"));

        when(repository.findById(1L)).thenReturn(Optional.of(dao));

        PaymentDTO result = service.getById(1L);

        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
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
    void create_statusPaid_generatesInvoice() {
        PaymentDTO dto = new PaymentDTO();
        dto.setBookingId(1L);
        dto.setAmount(new BigDecimal("500.00"));
        dto.setStatus("PAID");

        PaymentDAO savedDAO = new PaymentDAO();
        savedDAO.setId(1L);

        when(repository.save(any(PaymentDAO.class))).thenReturn(savedDAO);

        service.create(dto);

        verify(invoiceServiceImpl, times(1))
                .genereteInvoice(dto.getBookingId(), dto.getAmount());
        verify(repository, times(1)).save(any(PaymentDAO.class));
    }

    @Test
    void create_statusPaidCaseInsensitive_generatesInvoice() {
        PaymentDTO dto = new PaymentDTO();
        dto.setBookingId(2L);
        dto.setAmount(new BigDecimal("750.00"));
        dto.setStatus("paid"); // строчными буквами

        PaymentDAO savedDAO = new PaymentDAO();
        savedDAO.setId(1L);

        when(repository.save(any(PaymentDAO.class))).thenReturn(savedDAO);

        service.create(dto);

        verify(invoiceServiceImpl, times(1))
                .genereteInvoice(dto.getBookingId(), dto.getAmount());
    }

    @Test
    void create_statusNotPaid_doesNotGenerateInvoice() {
        PaymentDTO dto = new PaymentDTO();
        dto.setBookingId(3L);
        dto.setAmount(new BigDecimal("400.00"));
        dto.setStatus("PENDING");

        PaymentDAO savedDAO = new PaymentDAO();
        savedDAO.setId(1L);

        when(repository.save(any(PaymentDAO.class))).thenReturn(savedDAO);

        service.create(dto);

        verify(invoiceServiceImpl, never())
                .genereteInvoice(any(), any());
        verify(repository, times(1)).save(any(PaymentDAO.class));
    }

    @Test
    void create_nullStatus_doesNotGenerateInvoice() {
        PaymentDTO dto = new PaymentDTO();
        dto.setBookingId(4L);
        dto.setAmount(new BigDecimal("100.00"));
        dto.setStatus(null);

        PaymentDAO savedDAO = new PaymentDAO();
        savedDAO.setId(1L);

        when(repository.save(any(PaymentDAO.class))).thenReturn(savedDAO);

        service.create(dto);

        verify(invoiceServiceImpl, never())
                .genereteInvoice(any(), any());
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
}
