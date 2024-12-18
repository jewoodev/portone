package com.portone.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    ACTIVE("a", "정상"),
    SOLD_OUT("s", "품절"),
    OBSOLETE("o", "단종"),
    INACTIVE("i", "비활성화");

    private final String key;
    private final String description;
}
