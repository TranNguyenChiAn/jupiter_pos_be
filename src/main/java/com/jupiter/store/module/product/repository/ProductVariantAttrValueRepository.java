package com.jupiter.store.module.product.repository;

import com.jupiter.store.module.product.model.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductVariantAttrValueRepository extends JpaRepository<ProductAttributeValue, Integer> {
    @Query(value = "SELECT * FROM product_attribute_values pav WHERE pav.product_variant_id = :productVariantId and pav.attr_id = :attrId", nativeQuery = true)
    Optional<ProductAttributeValue> findByProductIdAndAttrId(@Param("productVariantId") Integer productVariantId, @Param("attrId") Integer attrId);


    @Query(value = "SELECT * FROM product_attribute_values pav WHERE pav.product_variant_id = :productVariantId", nativeQuery = true)
    List<ProductAttributeValue> findByProductVariantId(@Param("productVariantId") Integer productVariantId);


    @Query(value = "SELECT pa.id, pa.attribute_name as attr_name, pav.attr_value, u.id, u.unit_name " +
            "FROM product_attributes pa " +
            "LEFT JOIN product_attribute_values pav ON pa.id = pav.attr_id " +
            "LEFT JOIN UNITS U ON PAV.UNIT_ID = U.ID " +
            "WHERE pav.product_variant_id = :variantId " +
            "ORDER BY pa.id DESC " +
            "LIMIT :numberOfAttributes", nativeQuery = true)
    List<Object[]> findTopAttributesByVariantId(@Param("variantId") Integer variantId, @Param("numberOfAttributes") int numberOfAttributes);

    @Query(value = "SELECT pa.id, pa.attribute_name as attr_name, pav.attr_value, u.id, u.unit_name " +
            "FROM product_attributes pa " +
            "LEFT JOIN product_attribute_values pav ON pa.id = pav.attr_id " +
            "LEFT JOIN UNITS U ON PAV.UNIT_ID = U.ID " +
            "WHERE pav.product_variant_id = :variantId " +
            "ORDER BY pa.id DESC ", nativeQuery = true)
    List<Object[]> findAllAttributesByVariantId(@Param("variantId") Integer variantId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM product_attribute_values pav WHERE pav.attr_id = :attributeId", nativeQuery = true)
    void deleteByAttributeId(@Param("attributeId") Integer attributeId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE product_attribute_values SET unit_id = NULL WHERE unit_id = :unitId", nativeQuery = true)
    void updateUnitIdNull(@Param("unitId") Integer unitId);
}
