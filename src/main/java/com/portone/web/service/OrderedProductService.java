package com.portone.web.service;

import com.portone.domain.entity.OrderedProduct;
import com.portone.domain.repository.OrderedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderedProductService {
    private final OrderedProductRepository orderedProductRepository;

    public void bulkCreateOrderedProduct(List<OrderedProduct> orderedProducts) {
        orderedProductRepository.bulkCreate(orderedProducts);
    }
}
