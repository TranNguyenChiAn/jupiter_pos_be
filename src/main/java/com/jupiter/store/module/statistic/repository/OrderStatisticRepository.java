package com.jupiter.store.module.statistic.repository;

import com.jupiter.store.module.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderStatisticRepository extends JpaRepository<Order, Integer> {
    @Query(value = "SELECT" +
            "    o.order_status," +
            "    COUNT(*) AS total_orders " +
            "FROM orders o " +
            "WHERE o.order_date >= :startTime AND o.order_date <= :endTime " +
            "GROUP BY o.order_status", nativeQuery = true)
    List<Object[]> getOrderStatusStatistics(LocalDateTime startTime, LocalDateTime endTime);
}
