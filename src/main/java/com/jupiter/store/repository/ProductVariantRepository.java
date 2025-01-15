package com.jupiter.store.repository;

import com.jupiter.store.domain.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    @Query(value = "SELECT * FROM product_variants p WHERE p.product_id = :productId", nativeQuery = true)
    List<ProductVariant> findByProductId(@Param("productId") Long productId);

    @Query(value = "SELECT * FROM product_variants p WHERE p.id = :productVariantId", nativeQuery = true)
    Optional<ProductVariant> findByVariantId(@Param("productVariantId") Long productVariantId);
}
