package com.portone.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    REQUESTED("requested", "주문요청"),
    FAILED_PAYMENT("failed_payment", "결제실패"),
    PAID("paid", "결제완료"),
    PREPARED_PRODUCT("prepared_product", "상품준비중"),
    SHIPPED("shipped", "배송중"),
    DELIVERED("delivered", "배송완료"),
    CANCELED("canceled", "주문취소");

    private final String key;
    private final String description;
}
