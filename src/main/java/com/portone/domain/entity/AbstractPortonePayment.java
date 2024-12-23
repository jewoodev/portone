package com.portone.domain.entity;

import com.portone.domain.common.PayMethod;
import com.portone.domain.common.PayStatus;
import com.portone.domain.entity.converter.JsonConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.util.Map;

import static com.portone.domain.common.PayStatus.*;

@Getter
@MappedSuperclass
public abstract class AbstractPortonePayment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(1)
    private int desired_amount;

    @Length
    private String buyer_name;

    private String email;

    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    private boolean isPaidOk;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> meta;

    public void update(AbstractPortonePayment payment) {
        this.meta = payment.getMeta();
        this.payStatus = valueOf(meta.get("status").toString());
        isPaidOk = isPaid(meta);
    }

    private boolean isPaid(Map<String, Object> meta) {
        return desired_amount == Integer.parseInt(meta.get("amount").toString())
                && payStatus == PAID ? true : false;
    }
}
