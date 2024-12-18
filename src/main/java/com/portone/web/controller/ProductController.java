package com.portone.web.controller;

import com.portone.domain.entity.Product;
import com.portone.web.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public String main(
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        Pageable pageable = PageRequest.of(page, 9);
        Page<Product> productList = productService.findAll(pageable);
        int totalPages = productList.getTotalPages();
        int currentPage = productList.getNumber();
        int pageRange = 5; // 현재 페이지 기준 앞 뒤로 표시할 페이지 개수

        // 페이지 범위 계산
        int startPage = Math.max(0, currentPage - pageRange / 2);
        int endPage = Math.min(totalPages - 1, startPage + pageRange - 1);
        if (endPage - startPage < pageRange - 1) {
            startPage = Math.max(0, endPage - pageRange + 1);
        }

        model.addAttribute("productList", productList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "home";
    }

    @GetMapping("/api/products/load")
    public String loadProductForm() {
        return "product/jsonLoading";
    }

    @PostMapping("/api/products/load")
    public String loadProducts() {
        try {
            productService.loadProductsFromJson();
            log.info("Products loaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Failed to load products: " + e.getMessage());
        }

        return "redirect:/";
    }
}
