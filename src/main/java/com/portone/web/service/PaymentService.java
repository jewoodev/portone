package com.portone.web.service;

import com.portone.domain.entity.Payment;
import com.portone.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Transactional
@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public String makePayment(Payment payment) {
        paymentRepository.save(payment);
        return payment.getName();
    }

    public Payment findByUid(String uid) {
        return paymentRepository.findById(uid).orElseThrow(() -> new NoSuchElementException("존재하지 않는 결제 건입니다."));
    }
}
