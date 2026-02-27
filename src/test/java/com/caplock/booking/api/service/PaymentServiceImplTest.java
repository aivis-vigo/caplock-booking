package com.caplock.booking.api.service;

import com.caplock.booking.entity.StatusPaymentEnum;
import com.caplock.booking.entity.dao.PaymentEntity;
import com.caplock.booking.entity.dto.PaymentDto;
import com.caplock.booking.repository.PaymentRepository;
import com.caplock.booking.service.InvoiceService;
import com.caplock.booking.service.PaymentGateway;
import com.caplock.booking.service.PaymentService;
import com.caplock.booking.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    private PaymentRepository repository;
    private InvoiceService invoiceServiceImpl;
    private PaymentService service;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentRepository.class);
        invoiceServiceImpl = mock(InvoiceService.class);
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        ApplicationEventPublisher appEventPub = mock(ApplicationEventPublisher.class);
        service = new PaymentServiceImpl(repository, appEventPub, paymentGateway);
    }

    // ───── getAll ─────

    @Test
    void getAll_returnsListOfPaymentDtos() {
        PaymentEntity dao1 = new PaymentEntity();
        dao1.setId(1L);
        dao1.setAmount(new BigDecimal("100.00"));

        PaymentEntity dao2 = new PaymentEntity();
        dao2.setId(2L);
        dao2.setAmount(new BigDecimal("200.00"));

        when(repository.findAll()).thenReturn(List.of(dao1, dao2));

        List<PaymentDto> result = service.getAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAll_emptyRepository_returnsEmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<PaymentDto> result = service.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ───── getById ─────

    @Test
    void getById_existingId_returnsPaymentDto() {
        PaymentEntity dao = new PaymentEntity();
        dao.setId(1L);
        dao.setAmount(new BigDecimal("300.00"));

        when(repository.findById(1L)).thenReturn(Optional.of(dao));

        PaymentDto result = service.getById(1L).orElse(null);

        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
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
    void create_statusPaid_generatesInvoice() {
        PaymentDto dto = new PaymentDto();
        dto.setBookingId(1L);
        dto.setAmount(new BigDecimal("500.00"));
        dto.setStatus(StatusPaymentEnum.PAID);

        PaymentEntity savedDAO = new PaymentEntity();
        savedDAO.setId(1L);

        when(repository.save(any(PaymentEntity.class))).thenReturn(savedDAO);

        service.create(dto);

/*        verify(invoiceServiceImpl, times(1))
                .genereteInvoice(dto.getBookingId(), dto.getAmount());*/
        verify(repository, times(1)).save(any(PaymentEntity.class));
    }

    @Test
    @Deprecated
    @Disabled
    void create_statusPaidCaseInsensitive_generatesInvoice() {
        PaymentDto dto = new PaymentDto();
        dto.setBookingId(2L);
        dto.setAmount(new BigDecimal("750.00"));
        dto.setStatus(StatusPaymentEnum.PAID);

        PaymentEntity savedDAO = new PaymentEntity();
        savedDAO.setId(1L);

        when(repository.save(any(PaymentEntity.class))).thenReturn(savedDAO);

        service.create(dto);

//        verify(invoiceServiceImpl, times(1))
//                .genereteInvoice(dto.getBookingId(), dto.getAmount());
    }

    @Test
    @Deprecated
    @Disabled
    void create_statusNotPaid_doesNotGenerateInvoice() {
        PaymentDto dto = new PaymentDto();
        dto.setBookingId(3L);
        dto.setAmount(new BigDecimal("400.00"));
        dto.setStatus(StatusPaymentEnum.PENDING);

        PaymentEntity savedDAO = new PaymentEntity();
        savedDAO.setId(1L);

        when(repository.save(any(PaymentEntity.class))).thenReturn(savedDAO);

        service.create(dto);

/*        verify(invoiceServiceImpl, never())
                .genereteInvoice(any(), any());*/
        verify(repository, times(1)).save(any(PaymentEntity.class));
    }

    @Test
    @Deprecated
    @Disabled
    void create_nullStatus_doesNotGenerateInvoice() {
        PaymentDto dto = new PaymentDto();
        dto.setBookingId(4L);
        dto.setAmount(new BigDecimal("100.00"));
        dto.setStatus(null);

        PaymentEntity savedDAO = new PaymentEntity();
        savedDAO.setId(1L);

        when(repository.save(any(PaymentEntity.class))).thenReturn(savedDAO);

        service.create(dto);

/*        verify(invoiceServiceImpl, never())
                .genereteInvoice(any(), any());*/
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
