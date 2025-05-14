package com.jupiter.store.module.product.repository;

import com.jupiter.store.module.product.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    @Query(value = "DELETE FROM product_categories pc WHERE pc.product_id = :productId", nativeQuery = true)
    void deleteByProductId(@Param("productId") Integer productId);

    @Query(value = "DELETE FROM product_categories pc WHERE pc.product_id = :productId and pc.category_id = categoryId", nativeQuery = true)
    void deleteByProductIdAndCategoryId(@Param("productId") Integer productId, @Param("categoryId") Integer categoryId);
}