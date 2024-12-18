package com.portone.domain.repository;

import com.portone.domain.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    Optional<CartProduct> findByMemberUidAndProductName(String memberUid, String productName);

    Optional<CartProduct> findByUid(String uid);
}
