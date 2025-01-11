package com.jupiter.store.repository;

import com.jupiter.store.domain.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    @Query(value = "SELECT * FROM product_variants p WHERE productId = :productId and p.price = :price and p.quantity = :quantity and p.color = :color and p.sizeId = :sizeId", nativeQuery = true)
    ProductVariant findByVariant(@Param("productId") Long productId,@Param("price") int price,@Param("quantity") Integer quantity,@Param("color") String color,@Param("sizeId") Long sizeId);

    @Query(value = "SELECT * FROM product_variants p WHERE productId = :productId", nativeQuery = true)
    List<ProductVariant> findByProductId(@Param("productId") Long productId);
}
