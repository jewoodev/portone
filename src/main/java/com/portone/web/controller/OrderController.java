package com.portone.web.controller;

import com.portone.domain.dto.CustomUserDetails;
import com.portone.domain.entity.Order;
import com.portone.web.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;


@RequiredArgsConstructor
@Controller
public class OrderController {
    private final OrderService orderService;

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
}
