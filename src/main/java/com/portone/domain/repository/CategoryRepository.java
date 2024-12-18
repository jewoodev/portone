package com.portone.domain.repository;

import com.portone.domain.entity.Category;
import com.portone.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
