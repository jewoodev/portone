package com.portone.domain.entity;

import com.portone.domain.common.PayMethod;
import com.portone.domain.common.PayStatus;
import com.portone.domain.entity.converter.JsonConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Map;

import static com.portone.domain.common.PayStatus.PAID;
import static com.portone.domain.common.PayStatus.valueOf;

@NoArgsConstructor
@Getter
@MappedSuperclass
public abstract class AbstractPortonePayment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name; // 결제명

    @Min(1)
    private int desiredAmount;

    @Length
    private String buyerName;

    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    private boolean isPaidOk;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> meta;

    protected AbstractPortonePayment(String name, int desiredAmount, String buyerName, PayMethod payMethod, PayStatus payStatus) {
        this.name = name;
        this.desiredAmount = desiredAmount;
        this.buyerName = buyerName;
        this.payMethod = payMethod;
        this.payStatus = payStatus;
    }

    public void update(AbstractPortonePayment payment) {
        this.meta = payment.getMeta();
        this.payStatus = valueOf(meta.get("status").toString());
        isPaidOk = isPaid(meta);
    }

    private boolean isPaid(Map<String, Object> meta) {
        return desiredAmount == Integer.parseInt(meta.get("amount").toString())
                && payStatus == PAID ? true : false;
    }
}
