package com.portone.web.controller;

import com.portone.domain.dto.CustomUserDetails;
import com.portone.domain.entity.CartProduct;
import com.portone.web.service.CartProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CartProductController {
    private final CartProductService cartProductService;

    @ResponseBody
    @PostMapping("/api/cart/add")
    public Map<String, Object> addToCart(@RequestBody Map<String, String> payload,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            String productName = payload.get("productName");
            cartProductService.addToCart(customUserDetails.getMemberUid(), productName);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        response.put("status", "success");
        return response;
    }

    @GetMapping("/cart/{uid}")
    public String cartView(@PathVariable String uid, Model model) {
        List<CartProduct> cartProductList = cartProductService.findCartProduct(uid);
        model.addAttribute("cartProductList", cartProductList);
        return "product/cart";
    }

    @PostMapping("/cart/update")
    public String cartUpdate(@RequestParam Map<String, String> quantities,
                             @RequestParam(required = false) List<String> deleteUids,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        // CSRF 토큰 제거
        quantities.remove("_csrf");
        quantities.remove("deleteUids");

        // 1. 삭제 처리
        if (deleteUids != null && !deleteUids.isEmpty()) {
            cartProductService.deleteByUids(deleteUids);
            for (String deleteUid : deleteUids) {
                quantities.remove(deleteUid);
            }
        }

        // 2. 수량 업데이트 처리
        if (quantities != null && !quantities.isEmpty()) {
            cartProductService.updateQuantityMultiple(quantities);
        }

        String memberUid = customUserDetails.getMemberUid();

        return "redirect:/cart/" + memberUid;
    }
}
