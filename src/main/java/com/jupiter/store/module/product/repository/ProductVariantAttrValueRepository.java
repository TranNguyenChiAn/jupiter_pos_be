package com.jupiter.store.module.product.repository;

import com.jupiter.store.module.product.model.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductVariantAttrValueRepository extends JpaRepository<ProductAttributeValue, Integer> {
    @Query(value = "SELECT * FROM product_attribute_values pav WHERE pav.product_variant_id = :productVariantId and pav.attr_id = :attrId", nativeQuery = true)
    Optional<ProductAttributeValue> findByProductIdAndAttrId(@Param("productVariantId") Integer productVariantId, @Param("attrId") Integer attrId);


    @Query(value = "SELECT * FROM product_attribute_values pav WHERE pav.product_variant_id = :productVariantId", nativeQuery = true)
    List<ProductAttributeValue> findByProductVariantId(@Param("productVariantId") Integer productVariantId);
}
