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
    @Query(value = "DELETE FROM product_categories pc WHERE pc.product_id = :productId", nativeQuery = true)
    void deleteByProductId(@Param("productId") Long productId);
    @Query(value = "DELETE FROM product_categories pc WHERE pc.product_id = :productId and pc.category_id = categoryId", nativeQuery = true)
    void deleteByProductIdAndCategoryId(@Param("productId") Long productId, @Param("categoryId") Long categoryId);
}
