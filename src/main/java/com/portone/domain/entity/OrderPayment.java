package com.portone.domain.entity;

import jakarta.persistence.Entity;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class OrderPayment extends AbstractPortonePayment {
    private String order_uid;
}
