package com.portone.domain.repository;

import com.portone.domain.entity.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, String>, BulkInsertRepository {
}
