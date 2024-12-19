package com.portone.web.service;

import com.portone.domain.entity.CartProduct;
import com.portone.domain.repository.CartProductRepository;
import com.portone.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class CartProductService {
    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;

    @Transactional
    public String addToCart(String memberUid, String productName) {
        // 상품 검증
        productRepository.findByProductName(productName).orElseThrow(() -> new NoSuchElementException("존재하지 않는 옷입니다."));

        Optional<CartProduct> opCartProduct = cartProductRepository.findByMemberUidAndProductName(memberUid, productName);
        String cartProductUid;
        if (opCartProduct.isEmpty()) {
            CartProduct cartProduct = CartProduct.builder()
                    .cartProductUid(UUID.randomUUID().toString())
                    .productName(productName)
                    .memberUid(memberUid)
                    .quantity(1)
                    .build();

            cartProductUid = cartProductRepository.save(cartProduct).getCartProductUid();
        }
        else {
            CartProduct cartProduct = opCartProduct.get();
            cartProduct.increaseQuantity();
            cartProductUid = cartProduct.getCartProductUid();
        }

        return cartProductUid;
    }

    @Transactional(readOnly = true)
    public List<CartProduct> findCartProduct(String MemberUid) {
        List<CartProduct> cartProductList = cartProductRepository.findByMemberUidOrderByProductNameAsc(MemberUid);
        return cartProductList;
    }

    @Transactional
    public void deleteByUids(List<String> cartProductUids) {
        for (String cartProductUid : cartProductUids) {
            cartProductRepository.deleteByCartProductUid(cartProductUid);
        }
    }

    @Transactional
    public void updateQuantityMultiple(Map<String, String> quantities) {
        for (Map.Entry<String, String> entry : quantities.entrySet()) {
            cartProductRepository.updateQuantityByCartProductUid(entry.getKey(), Integer.parseInt(entry.getValue()));
        }
    }
}
