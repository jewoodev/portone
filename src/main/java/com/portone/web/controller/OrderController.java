package com.portone.web.controller;

import com.portone.domain.dto.CustomUserDetails;
import com.portone.domain.entity.Order;
import com.portone.domain.entity.OrderedProduct;
import com.portone.domain.repository.OrderedProductRepository;
import com.portone.web.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.DecimalFormat;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class OrderController {
    private final OrderService orderService;
    private final OrderedProductRepository orderedProductRepository;

    @PostMapping("/order/new")
    public String orderCreate(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Model model
    ) {
        String memberUid = customUserDetails.getMemberUid();
        Order order = orderService.createFromCart(memberUid);
        model.addAttribute("order", order);
        return "order/beforeConcern";
    }

    @GetMapping("/order/{ordersUid}")
    public String orderDetail(
            @PathVariable String ordersUid,
            Model model
    ) {
        Order order = orderService.findByOrderUid(ordersUid);
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedAmount = formatter.format(order.getTotalAmount());
        List<OrderedProduct> orderedProducts = orderedProductRepository.findByOrderUid(ordersUid);
        model.addAttribute("order", order);
        model.addAttribute("formattedAmount", formattedAmount);
        model.addAttribute("orderedProducts", orderedProducts);
        return "order/orderDetail";
    }
}
