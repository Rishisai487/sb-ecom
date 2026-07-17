package com.ecommerce.project.repositories;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findByProductNameContainsIgnoreCase(String keyword,Pageable pageable);

    Boolean existsByProductNameIgnoreCaseAndCategoryCategoryId(String productName, Long categoryId);

    Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageable);
}
