package com.portone.web.service;

import com.portone.domain.common.OrderStatus;
import com.portone.domain.entity.CartProduct;
import com.portone.domain.entity.Order;
import com.portone.domain.entity.OrderedProduct;
import com.portone.domain.entity.Product;
import com.portone.domain.repository.CartProductRepository;
import com.portone.domain.repository.OrderRepository;
import com.portone.domain.repository.OrderedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartProductRepository cartProductRepository;
    private final CartProductService cartProductService;
    private final ProductService productService;
    private final OrderedProductService orderedProductService;
    private final OrderedProductRepository orderedProductRepository;

    public Order findByOrderUid(String orderUid) {
        return orderRepository.findByOrderUid(orderUid).orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문입니다."));
    }

    /**
     * 장바구니에서 주문 생성을 할 수 있다.
     */
    @Transactional
    public Order createFromCart(String memberUid) {
        List<CartProduct> cartProductList = cartProductRepository.findByMemberUidOrderByProductNameAsc(memberUid);
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

        // 주문 이름 생성 후 Order에 삽입
        order.settingName(this.makeName(orderedProducts));

        // 연산된 totalAmount를 Order에 삽입
        order.settingTotalAmount(totalAmount);
        orderRepository.save(order); // 이 후 Create

        orderedProductService.bulkCreateOrderedProduct(orderedProducts);

        // 주문된 상품들 모두 카트에서 삭제
        cartProductService.deleteByUids(cartProductUids);

        return order;
    }

    public String nameOfOrder(Order order) {
        List<OrderedProduct> orderedProducts = orderedProductRepository.findByOrderUid(order.getOrderUid());
        return makeName(orderedProducts);
    }

    public String makeName(List<OrderedProduct> orderedProducts) {
        String firstProductName = orderedProducts.get(0).getProductName();
        int size = orderedProducts.size();
        if (size < 2) return firstProductName;

        return String.format("%s 외 %d건", firstProductName, size);
    }

    public void updateWhenPaidOk(String orderUid) {
        Order order = findByOrderUid(orderUid);
        order.updateStatus(OrderStatus.PAID);
    }
}
