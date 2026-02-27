package com.caplock.booking.service.impl;

import com.caplock.booking.entity.dto.PaymentDto;
import com.caplock.booking.entity.dao.PaymentEntity;
import com.caplock.booking.entity.StatusPaymentEnum;
import com.caplock.booking.event.paymentSucceededEvent;
import com.caplock.booking.repository.PaymentRepository;
import com.caplock.booking.service.PaymentGateway;
import com.caplock.booking.service.PaymentService;
import com.caplock.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PaymentGateway paymentGateway;

    @Override
    public PaymentDto create(PaymentDto dto) {
        // TODO: validations, e.g., check if booking exists, validate payment method, etc.
        // TODO: integrate with payment gateway, e.g., call payment API, handle response, etc.
        PaymentGateway.TransactionResult result = paymentGateway.processPayment(dto.getBookingId(), dto.getAmount(), dto.getMethod());
        PaymentEntity payment = Mapper.toEntity(dto);
        payment.setTransactionId(result.transactionId());
        payment.setPaidAt(LocalDateTime.now());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setProviderResponse(result.message());
        if (!result.success()) {
            payment.setStatus(StatusPaymentEnum.FAILED);
            log.info("Payment failed for bookingId: {}, amount: {}, method: {}, transactionId: {}, message: {}",
                    dto.getBookingId(), dto.getAmount(), dto.getMethod(), result.transactionId(), result.message());
        } else {
            log.info("Payment succeeded for bookingId: {}, amount: {}, method: {}, transactionId: {}",
                    dto.getBookingId(), dto.getAmount(), dto.getMethod(), result.transactionId());
            payment.setStatus(StatusPaymentEnum.PAID);
        }
        PaymentEntity saved = paymentRepository.save(payment);
        publishIfPaid(saved);
        return Mapper.toDto(saved);
    }

    @Override
    public Optional<PaymentDto> getById(Long id) {
        return paymentRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public List<PaymentDto> getAll() {
        return paymentRepository.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    public PaymentDto update(Long id, PaymentDto dto) {
        PaymentEntity entity = Mapper.toEntity(dto);
        entity.setId(id);

        boolean shouldPublish = paymentRepository.findById(id)
                .map(existing -> existing.getStatus() != StatusPaymentEnum.PAID && dto.getStatus() == StatusPaymentEnum.PAID)
                .orElse(dto.getStatus() == StatusPaymentEnum.PAID);

        PaymentEntity saved = paymentRepository.save(entity);
        if (shouldPublish) {
            publishIfPaid(saved);
        }
        return Mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }

    @Override
    public void refundPayment(Long paymentId) {
        //TODO: Implement refund logic, e.g., call payment gateway API, update payment status, etc.
        // For now, just a placeholder to indicate where refund logic would go.
        return;
    }

    private void publishIfPaid(PaymentEntity payment) {
        var success = payment.getStatus() == StatusPaymentEnum.PAID && payment.getBookingId() != null;
        eventPublisher.publishEvent(new paymentSucceededEvent(
                payment.getId(),
                payment.getBookingId(),
                success
        ));
    }
}
