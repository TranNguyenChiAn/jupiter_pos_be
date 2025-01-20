package com.jupiter.store.repository;

import com.jupiter.store.domain.Category;
import com.jupiter.store.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    @Query(value = "SELECT * FROM product_categories p WHERE product_id = :productId", nativeQuery = true)
    List<ProductCategory> findByAllProductId(@Param("productId") Long productId);

    @Query(value = "SELECT * FROM product_categories p WHERE product_id = :productId", nativeQuery = true)
    List<ProductCategory> findByProductId(@Param("productId") Long productId);
}
