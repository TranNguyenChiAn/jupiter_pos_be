package com.jupiter.store.module.statistic.repository;

import com.jupiter.store.module.product.model.ProductVariant;
import com.jupiter.store.module.statistic.dto.ProductSalesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductStatisticRepository extends JpaRepository<ProductVariant, Integer> {
    @Query(value = "SELECT p.product_name AS product_name, " +
                   "       SUM(od.sold_quantity) AS total_quantity_sold, " +
                   "       SUM(od.sold_price * od.sold_quantity) AS total_revenue " +
                   "FROM order_details od " +
                   "INNER JOIN product_variants pv ON pv.id = od.product_variant_id " +
                   "INNER JOIN products p ON p.id = pv.product_id " +
                   "INNER JOIN orders o ON o.id = od.order_id " +
                   "WHERE o.order_date BETWEEN :startTime AND :endTime " +
                   "GROUP BY p.product_name " +
                   "LIMIT 10", nativeQuery = true)
    List<Object[]> findTopSellingProducts(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
}
