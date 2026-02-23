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
        PaymentEntity saved = paymentRepository.save(Mapper.toEntity(dto));
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
                .map(existing -> existing.getStatus() != StatusPaymentEnum.Paid && dto.getStatus() == StatusPaymentEnum.Paid)
                .orElse(dto.getStatus() == StatusPaymentEnum.Paid);

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
        if (payment.getStatus() != StatusPaymentEnum.Paid || payment.getBookingId() == null) return;
        eventPublisher.publishEvent(new PaymentSucceededEvent(
                payment.getId(),
                payment.getBookingId()
        ));
    }
}
