package com.portone.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayMethod {
    CARD("card", "신용카드");

    private final String key;
    private final String description;
}
