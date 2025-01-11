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
    @Query(value = "SELECT * FROM product_categories p WHERE productId = :productId and p.categoryId = :categoryId", nativeQuery = true)
    ProductCategory findByCategoryId(@Param("productId") Long productId,@Param("categoryId") Long categoryId);

    @Query(value = "SELECT * FROM product_categories p WHERE productId = :productId", nativeQuery = true)
    List<ProductCategory> findByProductId(@Param("productId") Long productId);
}
