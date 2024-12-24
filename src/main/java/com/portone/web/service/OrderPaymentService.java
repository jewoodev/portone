package com.portone.web.service;

import com.portone.domain.entity.Member;
import com.portone.domain.entity.Order;
import com.portone.domain.entity.OrderPayment;
import com.portone.domain.repository.MemberRepository;
import com.portone.domain.repository.OrderPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class OrderPaymentService {
    private final OrderPaymentRepository orderPaymentRepository;
    private final MemberRepository memberRepository;
    private final OrderService orderService;

    public OrderPayment createFromOrder(Order order) {
        Member member = memberRepository.findByMemberUid(order.getMemberUid()).orElseThrow(() -> new NoSuchElementException("존재하지 않는 계정입니다."));
        String orderName = orderService.nameOfOrder(order);
        OrderPayment orderPayment = OrderPayment.createFromOrder(order, member, orderName);
        orderPaymentRepository.save(orderPayment);
        return orderPayment;
    }

    public OrderPayment findByOrderPaymentId(String paymentUid) {
        return orderPaymentRepository.findByPaymentUid(paymentUid).orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문입니다."));
    }
}
