package com.caplock.booking.repository;

import com.caplock.booking.entity.dao.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
