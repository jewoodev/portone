package com.portone.domain.repository;

import com.portone.domain.entity.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, String>, BulkInsertRepository {
    List<OrderedProduct> findByOrderUid(String orderUid);
}
