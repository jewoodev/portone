package com.portone.web.service;

import com.portone.domain.common.OrderStatus;
import com.portone.domain.entity.CartProduct;
import com.portone.domain.entity.Order;
import com.portone.domain.entity.OrderedProduct;
import com.portone.domain.entity.Product;
import com.portone.domain.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrdersRepository ordersRepository;
    private final CartProductService cartProductService;
    private final ProductService productService;
    private final OrderedProductService orderedProductService;

    public Order findByOrderUid(String orderUid) {
        return ordersRepository.findByOrderUid(orderUid).orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문입니다."));
    }

    /**
     * 장바구니에서 주문 생성을 할 수 있다.
     */
    @Transactional
    public Order createFromCart(String memberUid) {
        List<CartProduct> cartProductList = cartProductService.findByMemberUidOrderByProductNameAsc(memberUid);
        List<String> cartProductUids = new ArrayList<>(); // 주문한 상품은 일괄적으로 카트에서 삭제하기 위한 리스트
        int totalAmount = 0;

        Order order = Order.builder()
                .orderUid(UUID.randomUUID().toString())
                .memberUid(memberUid)
                .orderStatus(OrderStatus.REQUESTED)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();

        List<OrderedProduct> orderedProducts = new ArrayList<>();
        for (CartProduct cartProduct : cartProductList) {
            Product product = productService.findByProductName(cartProduct.getProductName());
            int productPrice = product.getPrice() * cartProduct.getQuantity(); // 장바구니에 담은 해당 제품 갯수만큼의 금액
            totalAmount += productPrice; // 주문에 입력할 총 주문금액
            OrderedProduct orderedProduct = OrderedProduct.builder()
                    .orderedProductUid(UUID.randomUUID().toString())
                    .orderUid(order.getOrderUid())
                    .productName(cartProduct.getProductName())
                    .price(productPrice)
                    .quantity(cartProduct.getQuantity())
                    .created_at(LocalDateTime.now())
                    .updated_at(LocalDateTime.now())
                    .build();
            orderedProducts.add(orderedProduct);
            cartProductUids.add(cartProduct.getCartProductUid());
        }

        // 연산된 totalAmount를 Order에 삽입
        order.settingTotalAmount(totalAmount);
        ordersRepository.save(order); // 이 후 Create

        orderedProductService.bulkCreateOrderedProduct(orderedProducts);

        // 주문된 상품들 모두 카트에서 삭제
        cartProductService.deleteByUids(cartProductUids);

        return order;
    }
}
