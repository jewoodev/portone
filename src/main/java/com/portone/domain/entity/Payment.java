package com.portone.domain.entity;

import com.portone.domain.common.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Payment {
    @Id @Column(name = "payment_id")
    private String id;

    @Length(max = 50)
    private String merchant_uid;

    @Length(max = 100)
    private String name;

    @Min(value = 1)
    private int amount;

    @Length(max = 9)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;


    private boolean isPaidOk;
}
