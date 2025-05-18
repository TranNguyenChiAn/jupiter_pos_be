package com.jupiter.store.module.product.repository;

import com.jupiter.store.module.product.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    @Query(value = "SELECT * FROM product_images p WHERE product_variant_id = :productId", nativeQuery = true)
    List<ProductImage> findByProductVariantId(@Param("productId") Integer productId);

    @Query(value = "DELETE FROM product_images pi WHERE pi.product_variant_id = :productId", nativeQuery = true)
    void deleteByProductVariantId(@Param("productId") Integer productId);
}