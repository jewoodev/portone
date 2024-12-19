package com.portone.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"memberUid, productName"})
)
@Entity
public class CartProduct {
    @Id
    private String cartProductUid;

    private String memberUid;
    private String productName;

    @Min(value = 1)
    private int quantity;

    /* 프로퍼티 업데이트 메서드 */
    public void increaseQuantity() {
        this.quantity += 1;
    }
}
