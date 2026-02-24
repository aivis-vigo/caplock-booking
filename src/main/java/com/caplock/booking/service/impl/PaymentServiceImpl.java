package com.caplock.booking.service.impl;

import com.caplock.booking.entity.dto.PaymentDto;
import com.caplock.booking.entity.dao.PaymentEntity;
import com.caplock.booking.entity.StatusPaymentEnum;
import com.caplock.booking.event.PaymentSucceededEvent;
import com.caplock.booking.repository.PaymentRepository;
import com.caplock.booking.service.PaymentService;
import com.caplock.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public PaymentDto create(PaymentDto dto) {
        // TODO: validations, e.g., check if booking exists, validate payment method, etc.
        // TODO: integrate with payment gateway, e.g., call payment API, handle response, etc.
        // For now, just a placeholder to indicate where payment processing logic would go.
        // boolean result = paymentGateway.processPayment(dto);
        // if (!result) {
        //     throw new RuntimeException("Payment processing failed");
        // } else {
        dto.setStatus(StatusPaymentEnum.PAID);
        // }
        PaymentEntity saved = paymentRepository.save(Mapper.toEntity(dto));
//        publishIfPaid(saved);
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
        eventPublisher.publishEvent(new PaymentSucceededEvent(
                payment.getId(),
                payment.getBookingId(),
                success
        ));
    }
}
