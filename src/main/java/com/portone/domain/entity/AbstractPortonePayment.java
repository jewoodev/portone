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
import java.util.UUID;

import static com.portone.domain.common.PayStatus.PAID;

@NoArgsConstructor
@Getter
@MappedSuperclass
public abstract class AbstractPortonePayment {
    @Id
    private String paymentUid;

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
        this.paymentUid = UUID.randomUUID().toString();
        this.name = name;
        this.desiredAmount = desiredAmount;
        this.buyerName = buyerName;
        this.payMethod = payMethod;
        this.payStatus = payStatus;
    }

    /**
     * 결제가 이루어지면 유효성을 검증한다.
      */
    public void check(Map<String, Object> paymentData) {
        Map<String, Object> responseData = (Map<String, Object>) paymentData.get("response");
        if (!isValid(responseData)) throw new IllegalArgumentException("올바르지 않은 결제가 요청되었습니다. 결제를 반려합니다.");
    }

    /**
     * 포트원 데이터의 결제 금액과 주문 금액이 일치하는 지 확인하고, 결제에 성공했는지 여부를 체크한다.
     */
    private boolean isValid(Map<String, Object> responseData) {
        int amount = (Integer) responseData.get("amount");
        String responseStatus = responseData.get("status").toString();
        /* 결제 금액이 포트원 응답 데이터와 일치하며 paid 상태임이 확인되는지를 반환 */
        return this.desiredAmount == amount && responseStatus.equals("paid") ? true : false;
    }

    /**
     * 결제 검증 후 결제 정보를 업데이트한다.
      */
    public void update(Map<String, Object> paymentData) {
        this.meta = paymentData;
        this.payStatus = PAID;
        this.isPaidOk = true;
    }
}
