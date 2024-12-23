package com.portone.web.controller;

import com.portone.domain.entity.Order;
import com.portone.domain.entity.OrderPayment;
import com.portone.web.service.OrderPaymentService;
import com.portone.web.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class OrderPaymentController {
    private final OrderPaymentService orderPaymentService;
    private final OrderService orderService;

    @PostMapping("/order/{ordersUid}/pay")
    public String orderPay(@PathVariable String ordersUid,
                                        Model model
    ) {
        Order order = orderService.findByOrderUid(ordersUid);
        // 경고 메세지 설정
        if (!order.canPay()) {
            model.addAttribute("payReponse" , "현재 결제를 할 수 없는 주문입니다.");
            return "order/detail";
        }
        OrderPayment payment = orderPaymentService.createFromOrder(order);
        model.addAttribute("payment", payment);
        return "order/orderPay";
    }
}
