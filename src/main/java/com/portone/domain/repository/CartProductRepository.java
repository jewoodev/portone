package com.portone.domain.repository;

import com.portone.domain.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    Optional<CartProduct> findByMemberUidAndProductName(String memberUid, String productName);

    Optional<CartProduct> findByUid(String uid);

    List<CartProduct> findByMemberUidOrderByProductNameAsc(String memberUid);

    void deleteByUid(String CartProductUid);

    @Modifying
    @Query("UPDATE CartProduct c SET c.quantity = :quantity WHERE c.uid = :uid")
    void updateQuantityByUid(@Param("uid") String uid, @Param("quantity") int quantity);
}
