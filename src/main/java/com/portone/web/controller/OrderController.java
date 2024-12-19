package com.portone.web.controller;

import com.portone.domain.dto.CustomUserDetails;
import com.portone.domain.entity.Order;
import com.portone.web.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order/new")
    public String orderCreate(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        String memberUid = customUserDetails.getMemberUid();
        Order order = orderService.createFromCart(memberUid);
        return "redirect:/order/" + order.getOrderUid() + "/pay";
    }

    @GetMapping("/order/{ordersUid}/pay")
    public String orderPay(@PathVariable String ordersUid,
                           RedirectAttributes redirectAttributes,
                           Model model
    ) {
        Order order = orderService.findByOrderUid(ordersUid);
        // 경고 메세지 설정
        redirectAttributes.addFlashAttribute("warningMessage", "구현 예정");
        model.addAttribute("orders", order);

        return "redirect:/order/" + ordersUid + "/pay";
    }

}
