package com.jupiter.store.module.statistic.repository;

import com.jupiter.store.module.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.print.attribute.standard.JobKOctets;

@Repository
public interface RevenueStatisticRepository extends JpaRepository<Payment, Integer> {
    @Query(value = "SELECT " +
            "    -- Doanh thu hôm nay\n" +
            "    (SELECT COALESCE(SUM(od.sold_price * od.sold_quantity), 0) " +
            "     FROM order_details od " +
            "              JOIN orders o ON o.id = od.order_id " +
            "     WHERE o.order_date::date = CURRENT_DATE) AS today_revenue, " +

            "    -- Lợi nhuận hôm nay\n" +
            "    (SELECT COALESCE(SUM((od.sold_price - pv.cost_price) * od.sold_quantity), 0) " +
            "     FROM order_details od " +
            "              JOIN orders o ON o.id = od.order_id " +
            "              JOIN product_variants pv ON pv.id = od.product_variant_id " +
            "     WHERE o.order_date::date = CURRENT_DATE) AS today_profit, " +

            "    -- Doanh thu hôm qua\n" +
            "    (SELECT COALESCE(SUM((od.sold_price - pv.cost_price) * od.sold_quantity), 0) " +
            "     FROM order_details od " +
            "              JOIN orders o ON o.id = od.order_id " +
            "              JOIN product_variants pv ON pv.id = od.product_variant_id " +
            "     WHERE o.order_date::date = CURRENT_DATE - INTERVAL '1 day') AS yesterday_revenue, " +

            "    -- Doanh thu tháng này\n" +
            "    (SELECT COALESCE(SUM(od.sold_price * od.sold_quantity), 0) " +
            "     FROM order_details od " +
            "              JOIN orders o ON o.id = od.order_id " +
            "     WHERE o.order_date >= DATE_TRUNC('month', CURRENT_DATE)) AS this_month_revenue, " +

            "    -- Doanh thu thasng truoc\n" +
            "    (SELECT COALESCE(SUM((od.sold_price - pv.cost_price) * od.sold_quantity), 0) " +
            "     FROM order_details od " +
            "              JOIN orders o ON o.id = od.order_id " +
            "              JOIN product_variants pv ON pv.id = od.product_variant_id " +
            "     WHERE o.order_date >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month') " +
            "     AND o.order_date < DATE_TRUNC('month', CURRENT_DATE)) AS last_month_revenue, " +

            "    -- Doanh thu trong 7 ngày qua\n" +
            "    (SELECT COALESCE(SUM(od.sold_price * od.sold_quantity), 0) " +
            "     FROM order_details od " +
            "             JOIN orders o ON o.id = od.order_id " +
            "     WHERE o.order_date::date >= CURRENT_DATE - INTERVAL '6 days') AS last_7_days_revenue"
            , nativeQuery = true)
    Object getMainStats();
}
