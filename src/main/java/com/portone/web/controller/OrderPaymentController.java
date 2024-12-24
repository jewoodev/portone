package com.portone.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portone.domain.entity.Order;
import com.portone.domain.entity.OrderPayment;
import com.portone.web.service.OrderPaymentService;
import com.portone.web.service.OrderService;
import com.portone.web.service.PortoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Controller
public class OrderPaymentController {
    private final OrderPaymentService orderPaymentService;
    private final OrderService orderService;
    private final PortoneService portoneService;

    @Value("${portone.pg.provider}")
    private String portonePgProvider;

    @Value("${portone.shop.id}")
    private String portoneShopId;

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
        Map<String, String> paymentProps = new HashMap<>();
        paymentProps.put("merchant_uid", String.valueOf(payment.getPaymentUid()));
        paymentProps.put("name", payment.getName());
        paymentProps.put("amount", String.valueOf(payment.getDesiredAmount()));
        paymentProps.put("buyer_name", payment.getBuyerName());
        paymentProps.put("buyer_email", "");
        paymentProps.put("pg", portonePgProvider);
        String payProps = null;
        try {
            payProps = new ObjectMapper().writeValueAsString(paymentProps);
        } catch (JsonProcessingException e) {
            model.addAttribute("errorMessage", "서버 내부적으로 문제가 발생했습니다. 잠시 후에 시도해주세요.");
        }
        model.addAttribute("payProps", payProps);
        model.addAttribute("verifyUrl", "/order/" + ordersUid + "/check/" + payment.getPaymentUid()); // 결제 검증 API URL
        model.addAttribute("portoneShopId", portoneShopId); // 가맹점 아이디
        return "order/orderPay";
    }

    @PatchMapping("/order/{ordersUid}/check/{paymentUid}")
    public ResponseEntity<Void> orderCheck(@PathVariable String ordersUid,
                                     @PathVariable String paymentUid,
                                     Model model) {
        try {
            portoneService.checkPayment(paymentUid);
        } catch (NoSuchElementException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        URI redirectUri = URI.create("/order/" + ordersUid);
        return ResponseEntity.ok().header("Location", redirectUri.toString()).build();
    }
}
