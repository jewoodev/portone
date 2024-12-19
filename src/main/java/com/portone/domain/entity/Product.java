package com.portone.domain.entity;

import com.portone.domain.common.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter
@Entity
public class Product {
    @Id @Column(unique = true)
    private String productName;

    @Lob
    private String description;

    @Min(value = 0)
    private int price;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private String productImgPath;
    private String categoryName;
}
