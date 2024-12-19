package com.portone.domain.entity;

import com.portone.domain.common.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter
@Table(indexes = @Index(name = "order_status", columnList = "order_status"))
@Entity
public class Orders {
    @Id
    private String orderUid;

    private String memberUid;

    @Min(1)
    private int totalAmount;

    @Enumerated
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;
}
