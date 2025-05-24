package com.jupiter.store.module.product.repository;

import com.jupiter.store.module.product.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    @Query(value = "SELECT * FROM product_variants pv WHERE pv.product_id = :productId", nativeQuery = true)
    List<ProductVariant> findByProductId(@Param("productId") Integer productId);

    @Query(value = "DELETE FROM product_variants pv WHERE pv.product_id = :productId", nativeQuery = true)
    void deleteByProductId(@Param("productId") Integer productId);

    @Query(value = "SELECT pv.id FROM product_variants pv", nativeQuery = true)
    List<Integer> findAllIds();
}
