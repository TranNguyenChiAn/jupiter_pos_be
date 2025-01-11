package com.jupiter.store.repository;

import com.jupiter.store.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Query(value = "SELECT * FROM product_images p WHERE productId = :producctId and p.imagePath = :image", nativeQuery = true)
    ProductImage findByImagePath(@Param("productId") Long productId, @Param("image") String image);

    @Query(value = "SELECT * FROM product_images p WHERE productId = :productId", nativeQuery = true)
    List<ProductImage> findByProductId(@Param("productId") Long productId);
}
