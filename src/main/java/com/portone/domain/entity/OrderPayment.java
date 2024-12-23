package com.portone.domain.entity;

import com.portone.domain.common.PayMethod;
import com.portone.domain.common.PayStatus;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class OrderPayment extends AbstractPortonePayment {
    private String orderUid;

    private OrderPayment(Order order, Member member, String orderName) {
        super(orderName, order.getTotalAmount(), member.getUsername(), PayMethod.CARD, PayStatus.READY);
        this.orderUid = order.getOrderUid();
    }

    public static OrderPayment createFromOrder(Order order, Member member, String orderName) {
        return new OrderPayment(order, member, orderName);
    }
}
