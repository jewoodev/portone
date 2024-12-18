package com.portone.web.controller;

import com.portone.domain.entity.Product;
import com.portone.web.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public String main(Model model) {
        List<Product> productList = productService.findAll();
        model.addAttribute("productList", productList);
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
