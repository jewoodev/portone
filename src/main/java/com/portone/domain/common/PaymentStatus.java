package com.portone.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    READY("READY", "미결제"),
    PAID("PAID", "결제완료"),
    CANCELED("CANCELED", "결제취소"),
    FAILED("FAILED", "결제실패");

    private final String key;
    private final String description;
}
