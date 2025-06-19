package com.jupiter.store.module.statistic.repository;

import com.jupiter.store.module.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerStatisticRepository extends JpaRepository<Customer, Integer> {
    @Query(value = "-- 1. Tổng đơn hàng, tổng chi và tổng nợ theo khách hàng\n " +
            "WITH base AS ( " +
            "    SELECT " +
            "        o.customer_id, " +
            "        COUNT(DISTINCT o.id) AS order_count, " +
            "        SUM( " +
            "                CASE " +
            "                    WHEN p.remaining < 0 THEN (p.paid + p.remaining) " +
            "                    ELSE p.paid " +
            "                    END " +
            "        ) AS total_spent, " +
            "        SUM(o.total_amount) AS total_order_amount " +
            "    FROM orders o " +
            "             INNER JOIN payments p ON p.order_id = o.id " +
            "    WHERE p.date BETWEEN :startTime AND :endTime " +
            "    GROUP BY o.customer_id " +
            "    ), " +
            "-- 2. Tổng số lượng sản phẩm theo khách hàng\n " +
            "     detail AS ( " +
            "         SELECT " +
            "             o.customer_id, " +
            "             SUM(od.sold_quantity) AS total_quantity_bought " +
            "         FROM orders o " +
            "                  JOIN order_details od ON od.order_id = o.id " +
            "         GROUP BY o.customer_id " +
            "     ) " +
            "-- 3. JOIN tất cả lại với customers\n " +
            "SELECT " +
            "    c.customer_name, " +
            "    COALESCE(b.order_count, 0) AS order_count, " +
            "    COALESCE(b.total_spent, 0) AS total_spent, " +
            "    COALESCE(b.total_order_amount, 0) - COALESCE(b.total_spent, 0) AS total_debt, " +
            "    COALESCE(d.total_quantity_bought, 0) AS total_quantity_bought " +
            "FROM customers c " +
            "INNER JOIN base b ON b.customer_id = c.id " +
            "INNER JOIN detail d ON d.customer_id = c.id ", nativeQuery = true)
    List<Object[]> getCustomerData(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
}
