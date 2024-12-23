package com.portone.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayStatus {
    READY("card", "신용카드"),
    PAID("paid", "결제 완료"),
    CANCELLED("cancelled", "결제 취소"),
    FAILED("failed", "결제 실패");

    private final String key;
    private final String description;
}
