package com.jupiter.store.module.statistic.repository;

import com.jupiter.store.module.product.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductStatisticRepository extends JpaRepository<ProductVariant, Integer> {
    @Query(value = "SELECT " +
            "    p.product_name || " +
            "    CASE " +
            "        WHEN COUNT(pav.attr_value) > 0 THEN ' - ' || STRING_AGG(pav.attr_value, ', ') " +
            "        ELSE '' " +
            "        END AS product_name, " +
            "    pv.quantity AS inventory_count " +
            "FROM product_variants pv " +
            "LEFT JOIN product_attribute_values pav ON pav.product_variant_id = pv.id " +
            "LEFT JOIN products p ON pv.product_id = p.id " +
            "WHERE pv.status != 'DA_XOA' AND p.status != 'DA_XOA' " +
            "GROUP BY pv.id, p.product_name, pv.quantity " +
            "ORDER BY pv.quantity " +
            "LIMIT 10 ", nativeQuery = true)
    List<Object[]> getLowInventoryProduct();

    @Query(value = "SELECT " +
            "    p.product_name || " +
            "    CASE " +
            "        WHEN COUNT(pav.attr_value) > 0 THEN ' - ' || STRING_AGG(pav.attr_value, ', ') " +
            "        ELSE '' " +
            "        END AS product_name, " +
            "    pv.quantity AS inventory_count " +
            "FROM product_variants pv " +
            "LEFT JOIN product_attribute_values pav ON pav.product_variant_id = pv.id " +
            "LEFT JOIN products p ON pv.product_id = p.id " +
            "WHERE pv.status != 'DA_XOA' AND p.status != 'DA_XOA' " +
            "GROUP BY pv.id, p.product_name, pv.quantity " +
            "ORDER BY pv.quantity " +
            "LIMIT 10 ", nativeQuery = true)
    List<Object[]> getDeadStockData();;
}
