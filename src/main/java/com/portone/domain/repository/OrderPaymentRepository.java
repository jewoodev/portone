package com.portone.domain.repository;

import com.portone.domain.entity.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderPaymentRepository extends JpaRepository<OrderPayment, String> {
    Optional<OrderPayment> findByPaymentUid(String paymentUid);
}
