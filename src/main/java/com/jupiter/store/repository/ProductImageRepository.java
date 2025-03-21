package com.jupiter.store.repository;

import com.jupiter.store.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Query(value = "SELECT * FROM product_images p WHERE product_id = :productId", nativeQuery = true)
    List<ProductImage> findByProductId(@Param("productId") Long productId);
    @Query(value = "DELETE FROM product_images pi WHERE pi.product_id = :productId", nativeQuery = true)
    void deleteByProductId(@Param("productId") Long productId);
}