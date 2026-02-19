package com.caplock.booking.api.repository;

import com.caplock.booking.entity.dao.PaymentDAO;
import com.caplock.booking.repository.PaymentMockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMockRepositoryTest {

    private PaymentMockRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PaymentMockRepository();
    }

    @Test
    void save_newPayment_assignsIdAndStores() {
        PaymentDAO payment = new PaymentDAO();
        payment.setAmount(new BigDecimal("100.00"));

        PaymentDAO saved = repository.save(payment);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getId());
    }

    @Test
    void save_paymentWithExistingId_doesNotOverrideId() {
        PaymentDAO payment = new PaymentDAO();
        payment.setId(42L);
        payment.setAmount(new BigDecimal("200.00"));

        PaymentDAO saved = repository.save(payment);

        assertEquals(42L, saved.getId());
    }

    @Test
    void save_multiplePaymentsWithoutId_incrementsIdSequentially() {
        PaymentDAO first = repository.save(new PaymentDAO());
        PaymentDAO second = repository.save(new PaymentDAO());
        PaymentDAO third = repository.save(new PaymentDAO());

        assertEquals(1L, first.getId());
        assertEquals(2L, second.getId());
        assertEquals(3L, third.getId());
    }

    @Test
    void save_updatesExistingPayment() {
        PaymentDAO payment = new PaymentDAO();
        payment.setAmount(new BigDecimal("100.00"));
        repository.save(payment);

        payment.setAmount(new BigDecimal("999.00"));
        repository.save(payment);

        assertEquals(1, repository.findAll().size());
        assertEquals(0, new BigDecimal("999.00").compareTo(
                repository.findById(payment.getId()).get().getAmount()
        ));
    }

    @Test
    void findById_existingId_returnsPayment() {
        PaymentDAO payment = new PaymentDAO();
        payment.setAmount(new BigDecimal("300.00"));
        repository.save(payment);

        Optional<PaymentDAO> found = repository.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(0, new BigDecimal("300.00").compareTo(found.get().getAmount()));
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        Optional<PaymentDAO> found = repository.findById(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_emptyStorage_returnsEmptyList() {
        List<PaymentDAO> result = repository.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_afterSavingPayments_returnsAll() {
        repository.save(new PaymentDAO());
        repository.save(new PaymentDAO());
        repository.save(new PaymentDAO());

        List<PaymentDAO> result = repository.findAll();

        assertEquals(3, result.size());
    }

    @Test
    void findAll_returnsMutableList() {
        repository.save(new PaymentDAO());

        List<PaymentDAO> result = repository.findAll();

        assertDoesNotThrow(() -> result.add(new PaymentDAO()));
    }

    @Test
    void deleteById_existingId_removesPayment() {
        PaymentDAO payment = repository.save(new PaymentDAO());
        Long id = payment.getId();

        repository.deleteById(id);

        assertTrue(repository.findById(id).isEmpty());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void deleteById_nonExistingId_doesNotThrow() {
        assertDoesNotThrow(() -> repository.deleteById(999L));
    }

    @Test
    void deleteById_doesNotAffectOtherPayments() {
        PaymentDAO first = repository.save(new PaymentDAO());
        PaymentDAO second = repository.save(new PaymentDAO());

        repository.deleteById(first.getId());

        assertEquals(1, repository.findAll().size());
        assertTrue(repository.findById(second.getId()).isPresent());
    }
}