package com.jupiter.store.module.product.repository;

import com.jupiter.store.module.product.dto.ProductVariantReadDTO;
import com.jupiter.store.module.product.model.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query(value = "SELECT pv FROM product_variants pv INNER JOIN products p ON p.id = pv.product_id " +
            "WHERE lower(p.product_name) LIKE lower(concat('%', :productName, '%')) ORDER BY pv.last_modified_date DESC", nativeQuery = true)
    Page<ProductVariant> findByProductNameContainingIgnoreCase(@Param("productName") String productName, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE product_variants pv SET status = 'DELETED' WHERE pv.product_id = :productId", nativeQuery = true)
    void softDeleteByProductId(@Param("productId") Integer productId);

    @Query(value = "SELECT * FROM product_variants pv WHERE pv.product_id IN :productIds", nativeQuery = true)
    List<ProductVariant> findByProductIdIn(@Param("productIds") List<Integer> productIds);
}
