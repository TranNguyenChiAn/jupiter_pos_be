package com.jupiter.store.module.statistic.repository;

import com.jupiter.store.module.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RevenueStatisticRepository extends JpaRepository<Payment, Integer> {
    @Query(value = "SELECT " +
            "    -- Doanh thu hôm nay (đã thanh toán)\n" +
            "    (SELECT COALESCE(SUM(p.paid), 0) " +
            "     FROM payments p " +
            "     WHERE p.status = 'THANH_TOAN_THANH_CONG' " +
            "       AND p.date >= CURRENT_DATE " +
            "       AND p.date < CURRENT_DATE + INTERVAL '1 day') AS today_revenue, " +

            "    -- Lợi nhuận hôm nay (từ đơn đã thanh toán)\n" +
            "    (SELECT " +
            "         SUM(p.paid) - SUM(od.sold_quantity * pv.cost_price) AS profit " +
            "     FROM payments p " +
            "              JOIN orders o ON p.order_id = o.id " +
            "              JOIN order_details od ON o.id = od.order_id " +
            "              JOIN product_variants pv ON od.product_variant_id = pv.id " +
            "     WHERE " +
            "         p.status = 'THANH_TOAN_THANH_CONG' " +
            "       AND p.date >= CURRENT_DATE " +
            "       AND p.date < CURRENT_DATE + INTERVAL '1 day') AS today_profit, " +

            "    -- Doanh thu hôm qua (đã thanh toán)\n" +
            "    (SELECT COALESCE(SUM(p.paid), 0) " +
            "     FROM payments p " +
            "     WHERE p.status = 'THANH_TOAN_THANH_CONG' " +
            "       AND p.date::date = CURRENT_DATE - INTERVAL '1 day') AS yesterday_revenue, " +

            "    -- Doanh thu tháng này (đã thanh toán)\n" +
            "    (SELECT COALESCE(SUM(p.paid), 0) " +
            "     FROM payments p " +
            "     WHERE p.status = 'THANH_TOAN_THANH_CONG' " +
            "       AND p.date >= DATE_TRUNC('month', CURRENT_DATE)) AS this_month_revenue, " +

            "    -- Doanh thu tháng trước (đã thanh toán) \n " +
            "    (SELECT COALESCE(SUM(p.paid), 0) " +
            "     FROM payments p " +
            "     WHERE p.status = 'THANH_TOAN_THANH_CONG' " +
            "       AND p.date >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month') " +
            "       AND p.date < DATE_TRUNC('month', CURRENT_DATE)) AS last_month_revenue, " +

            "    -- Doanh thu trong 7 ngày qua (đã thanh toán) \n " +
            "    (SELECT COALESCE(SUM(p.paid), 0) " +
            "     FROM payments p " +
            "     WHERE p.status = 'THANH_TOAN_THANH_CONG' " +
            "       AND p.date::date >= CURRENT_DATE - INTERVAL '6 days') AS last_7_days_revenue, " +

            "    -- Tổng số đơn hàng hôm nay \n " +
            "    (SELECT COUNT(o.id) " +
            "     FROM orders o " +
            "     WHERE o.order_date::date = CURRENT_DATE) AS today_total_orders"
            , nativeQuery = true)
    Object getMainStats();

    @Query(value = "SELECT " +
            "    p.payment_method AS paymentMethod, " +
            "    CAST(COUNT(p.id) AS INTEGER) AS transactionCount, " +
            "    SUM(p.paid) AS totalAmount " +
            "FROM payments p " +
            "WHERE p.status = 'THANH_TOAN_THANH_CONG' AND CAST(p.CREATED_DATE AS DATE) BETWEEN :startDate AND :endDate " +
            "GROUP BY p.payment_method", nativeQuery = true)
    List<Object[]> getPaymentMethodsData(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
