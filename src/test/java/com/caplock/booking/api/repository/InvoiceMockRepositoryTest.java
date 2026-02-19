package com.caplock.booking.api.repository;

import com.caplock.booking.entity.dao.InvoiceDAO;
import com.caplock.booking.repository.InvoiceMockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceMockRepositoryTest {

    private InvoiceMockRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InvoiceMockRepository();
    }

    @Test
    void save_newInvoice_assignsIdAndStores() {
        InvoiceDAO invoice = new InvoiceDAO();
        invoice.setAmount(BigDecimal.valueOf(100.0));

        InvoiceDAO saved = repository.save(invoice);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getId());
    }

    @Test
    void save_invoiceWithExistingId_doesNotOverrideId() {
        InvoiceDAO invoice = new InvoiceDAO();
        invoice.setId(42L);
        invoice.setAmount(BigDecimal.valueOf(200.0));

        InvoiceDAO saved = repository.save(invoice);

        assertEquals(42L, saved.getId());
    }

    @Test
    void save_multipleInvoicesWithoutId_incrementsIdSequentially() {
        InvoiceDAO first = repository.save(new InvoiceDAO());
        InvoiceDAO second = repository.save(new InvoiceDAO());
        InvoiceDAO third = repository.save(new InvoiceDAO());

        assertEquals(1L, first.getId());
        assertEquals(2L, second.getId());
        assertEquals(3L, third.getId());
    }

    @Test
    void save_updatesExistingInvoice() {
        InvoiceDAO invoice = new InvoiceDAO();
        invoice.setAmount(new BigDecimal("100.00"));
        repository.save(invoice);

        invoice.setAmount(new BigDecimal("999.00"));
        repository.save(invoice);

        assertEquals(1, repository.findAll().size());
        assertEquals(0, new BigDecimal("999.00").compareTo(
                repository.findById(invoice.getId()).get().getAmount()
        ));
    }

    @Test
    void findById_existingId_returnsInvoice() {
        InvoiceDAO invoice = new InvoiceDAO();
        invoice.setAmount(new BigDecimal("300.00"));
        repository.save(invoice);

        Optional<InvoiceDAO> found = repository.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(0, new BigDecimal("300.00").compareTo(found.get().getAmount()));
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        Optional<InvoiceDAO> found = repository.findById(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_emptyStorage_returnsEmptyList() {
        List<InvoiceDAO> result = repository.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_afterSavingInvoices_returnsAll() {
        repository.save(new InvoiceDAO());
        repository.save(new InvoiceDAO());
        repository.save(new InvoiceDAO());

        List<InvoiceDAO> result = repository.findAll();

        assertEquals(3, result.size());
    }

    @Test
    void findAll_returnsMutableList() {
        repository.save(new InvoiceDAO());

        List<InvoiceDAO> result = repository.findAll();
        assertDoesNotThrow(() -> result.add(new InvoiceDAO()));
    }

    @Test
    void deleteById_existingId_removesInvoice() {
        InvoiceDAO invoice = repository.save(new InvoiceDAO());
        Long id = invoice.getId();

        repository.deleteById(id);

        assertTrue(repository.findById(id).isEmpty());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void deleteById_nonExistingId_doesNotThrow() {
        assertDoesNotThrow(() -> repository.deleteById(999L));
    }

    @Test
    void deleteById_doesNotAffectOtherInvoices() {
        InvoiceDAO first = repository.save(new InvoiceDAO());
        InvoiceDAO second = repository.save(new InvoiceDAO());

        repository.deleteById(first.getId());

        assertEquals(1, repository.findAll().size());
        assertTrue(repository.findById(second.getId()).isPresent());
    }
}
