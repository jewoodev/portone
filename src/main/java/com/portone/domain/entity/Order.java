package com.portone.domain.entity;

import com.portone.domain.common.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "orders", indexes = @Index(name = "order_status", columnList = "order_status"))
@Entity
public class Order extends BaseEntity {
    @Id
    private String orderUid;

    private String memberUid;

    private String name;

    @Min(1)
    private int totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Builder
    private Order(String orderUid, String memberUid, int totalAmount, OrderStatus orderStatus) {
        this.orderUid = orderUid;
        this.memberUid = memberUid;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
    }

    public void settingTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
    public void settingName(String name) {
        this.name = name;
    }

    public boolean canPay() {
        if (this.orderStatus == OrderStatus.REQUESTED || this.orderStatus == OrderStatus.FAILED_PAYMENT) {
            return true;
        }
        return false;
    }

    /**
     * 결제가 성공적으로 처리되면 주문 상태를 결제완료 상태로 업데이트한다.
     */
    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
