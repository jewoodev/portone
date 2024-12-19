package com.portone.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portone.domain.common.ProductStatus;
import com.portone.domain.entity.CartProduct;
import com.portone.domain.entity.Category;
import com.portone.domain.entity.Product;
import com.portone.domain.repository.CartProductRepository;
import com.portone.domain.repository.CategoryRepository;
import com.portone.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final String staticDir = "src/main/resources/static/product/";

    @Transactional
    public void loadProductsFromJson() throws Exception {
        String baseUrl = "https://raw.githubusercontent.com/pyhub-kr/dump-data/main/django-shopping-with-iamport/";
        String jsonUrl = baseUrl + "product-list.json";

        //JSON 데이터 읽기
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(new URL(jsonUrl));

        Set<String> categorySet = new HashSet<>();

        for (JsonNode productNode : root) {
            String name = productNode.get("name").asText();
            int price = productNode.get("price").asInt();
            String description = productNode.get("desc").asText();
            String photoPath = productNode.get("photo_path").asText();
            String photoUrl = baseUrl + photoPath;
            String categoryName = productNode.get("category_name").asText();
            categorySet.add(categoryName);

            // 사진 다운로드
            String savedPath = downloadPhoto(photoUrl, photoPath);

            // Product 엔티티 생성
            Product product = Product.builder()
                    .productName(name)
                    .price(price)
                    .status(ProductStatus.INACTIVE)
                    .description(description)
                    .categoryName(categoryName)
                    .productImgPath(savedPath)
                    .build();

            productRepository.save(product);
        }

        for (String name : categorySet) {
            Category category = new Category(name);
            categoryRepository.save(category);
        }
    }

    private String downloadPhoto(String photoUrl, String photoName) throws Exception {
        try (InputStream in = new URL(photoUrl).openStream()) {
            String filePath = staticDir + photoName;
            Files.copy(in, Paths.get(filePath));
            return "http://localhost:8080/images/" + photoName; // Static 경로 반환
        }
    }

    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> searchProductByName(String name, Pageable pageable) {
        return productRepository.findByNameContaining(name, pageable);
    }

    @Transactional(readOnly = true)
    public Product findByProductName(String productName) {
        return productRepository.findByProductName(productName).orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));
    }
}
