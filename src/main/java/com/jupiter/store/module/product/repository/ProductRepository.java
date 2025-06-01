package com.jupiter.store.module.product.repository;

import com.jupiter.store.module.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT DISTINCT p.* FROM products p LEFT JOIN product_variants pv ON p.id = pv.product_id " +
            "WHERE LOWER(p.product_name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(pv.sku) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(pv.barcode) LIKE LOWER(CONCAT('%', :search, '%'))",
            countQuery = "SELECT COUNT(DISTINCT p.id) FROM products p LEFT JOIN product_variants pv ON p.id = pv.product_id " +
                    "WHERE LOWER(p.product_name) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "OR LOWER(pv.sku) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "OR LOWER(pv.barcode) LIKE LOWER(CONCAT('%', :search, '%'))",
            nativeQuery = true)
    Page<Product> searchProduct(String search, Pageable pageable);
}
