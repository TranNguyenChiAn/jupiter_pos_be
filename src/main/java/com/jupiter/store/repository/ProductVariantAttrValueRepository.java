package com.jupiter.store.repository;

import com.jupiter.store.model.ProductVariantAttrValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductVariantAttrValueRepository extends JpaRepository<ProductVariantAttrValue, Long> {
    @Query(value = "SELECT * FROM product_variant_attr_value pvav WHERE pvav.product_variant_id = :productVariantId and pvav.attri", nativeQuery = true)
    Optional<ProductVariantAttrValue> findByProductIdAndAttrId(@Param("productVariantId") Long productVariantId, @Param("attrId") Long attrId);
}
