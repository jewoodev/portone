package com.portone.domain.entity;

import com.portone.domain.common.PaymentStatus;
import com.portone.domain.dto.PaymentDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Payment {
    @Id @Column(name = "payment_id")
    private String uid;

    @Length(max = 100)
    private String name;

    @Min(value = 1)
    private int amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private boolean isPaidOk;

    /* 객체 변환 메서드 */
    public PaymentDto toPaymentDto() {
        return PaymentDto.builder()
                .name(this.name)
                .amount(String.valueOf(this.amount))
                .build();
    }
}
