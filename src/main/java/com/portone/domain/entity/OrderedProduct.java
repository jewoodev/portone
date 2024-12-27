package com.portone.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter
@Entity
public class OrderedProduct extends BaseEntity {
    @Id
    private String orderedProductUid;

    private String orderUid;

    private String productName;

    @Min(1)
    private int price;

    @Min(1)
    private int quantity;
}
