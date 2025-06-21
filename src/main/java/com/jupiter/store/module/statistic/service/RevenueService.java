package com.jupiter.store.module.statistic.service;

import com.jupiter.store.module.statistic.dto.CustomerResponseDTO;
import com.jupiter.store.module.statistic.dto.NetRevenueDTO;
import com.jupiter.store.module.statistic.dto.TodayResponseDTO;
import com.jupiter.store.module.statistic.dto.ProductSalesDTO;
import com.jupiter.store.module.statistic.repository.CustomerStatisticRepository;
import com.jupiter.store.module.statistic.repository.ProductStatisticRepository;
import com.jupiter.store.module.statistic.repository.RevenueStatisticRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RevenueService {
    @Autowired
    private RevenueStatisticRepository revenueStatisticRepository;

    @Autowired
    private ProductStatisticRepository productStatisticRepository;

    @Autowired
    private CustomerStatisticRepository customerStatisticRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TodayResponseDTO getTodayData() {
        Object result = revenueStatisticRepository.getMainStats();
        Object[] row = (Object[]) result;
        long today = row[0] != null ? ((BigDecimal) row[0]).longValue() : 0L;
        long todayProfit = row[1] != null ? ((BigDecimal) row[1]).longValue() : 0L;
        long yesterday = row[2] != null ? ((BigDecimal) row[2]).longValue() : 0L;
        long thisMonth = row[3] != null ? ((BigDecimal) row[3]).longValue() : 0L;
        long lastMonth = row[4] != null ? ((BigDecimal) row[4]).longValue() : 0L;
        long last7Days = row[5] != null ? ((BigDecimal) row[5]).longValue() : 0L;
        long totalOrder = row[6] != null ? ((Long) row[6]) : 0L;

        double todayChange = (yesterday == 0)
                    ? 100.00
                    : Math.round(((double) (today - yesterday) / yesterday) * 10000) / 100.0;

        double monthChange = (lastMonth == 0)
                ? 100.00
                : Math.round(((double) (thisMonth - lastMonth) / lastMonth) * 10000) / 100.0;

        return new TodayResponseDTO(
                today,
                todayProfit,
                thisMonth,
                last7Days,
                todayChange,
                monthChange,
                totalOrder
        );
    }

    public List<NetRevenueDTO> getNetRevenue(
            String groupByLabel,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        List<NetRevenueDTO> netRevenueData = new ArrayList<>();
        String groupByExpr;
        String labelExpr;
        String groupBySortExpr;
        switch (groupByLabel.toLowerCase()) {
            case "day" -> {
                labelExpr = "TO_CHAR(p.date, 'DD/MM')";
                groupByExpr = labelExpr;
                groupBySortExpr = "MIN(p.date)";
            }
            case "hour" -> {
                labelExpr = "TO_CHAR(DATE_TRUNC('hour', p.date), 'HH24:00')";
                groupByExpr = "DATE_TRUNC('hour', p.date)";
                groupBySortExpr = "MIN(p.date)";
            }
            case "weekday" -> {
                labelExpr = """
                    CASE EXTRACT(DOW FROM p.date)::int
                        WHEN 0 THEN 'Chủ nhật'
                        WHEN 1 THEN 'Thứ 2'
                        WHEN 2 THEN 'Thứ 3'
                        WHEN 3 THEN 'Thứ 4'
                        WHEN 4 THEN 'Thứ 5'
                        WHEN 5 THEN 'Thứ 6'
                        WHEN 6 THEN 'Thứ 7'
                    END
                    """;
                groupByExpr = "EXTRACT(DOW FROM p.date)";
                groupBySortExpr = "MIN(p.date)";
            }
            default -> throw new IllegalArgumentException("Invalid groupByLabel: " + groupByLabel);
        }

        String sql = String.format("""
            WITH revenue_data AS (
                SELECT
                    %s AS label,
                    %s AS sort_time,
                    SUM(p.paid) AS revenue
                FROM payments p
                WHERE p.status = 'THANH_TOAN_THANH_CONG'
                  AND p.date BETWEEN :startTime AND :endTime
                GROUP BY %s
            )
            SELECT label, revenue
            FROM revenue_data
            ORDER BY sort_time
            """, labelExpr, groupBySortExpr, groupByExpr);

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);

        List<Object[]> results = query.getResultList();
        for (Object[] row : results) {
            String dataLabel = row[0] != null ? ((String) row[0]) : "";
            long revenue = row[1] != null ? ((BigDecimal) row[1]).longValue() : 0L;
            netRevenueData.add(new NetRevenueDTO(dataLabel, revenue));
        }
        return netRevenueData;
    }

    public List<ProductSalesDTO> getProductData(
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        List<ProductSalesDTO> productSales = new ArrayList<>();
        List<Object[]> results = productStatisticRepository.findTopSellingProducts(startTime, endTime);
        for (Object[] row : results) {
            String productName = row[0] != null ? ((String) row[0]) : "";
            long totalQuantity = row[1] != null ? ((long) row[1]) : 0L;
            long revenue = row[2] != null ? ((BigDecimal) row[2]).longValue() : 0L;
            productSales.add(new ProductSalesDTO(productName, totalQuantity, revenue));
        }
        return productSales;
    }

    public List<CustomerResponseDTO> getCustomersData(
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        List<CustomerResponseDTO> customerData = new ArrayList<>();
        List<Object[]> results = customerStatisticRepository.getCustomerData(startTime, endTime);
        for(Object[] row : results){
            String customerName = row[0] != null ? ((String) row[0]) : "";
            long totalOrder = row[1] != null ? ((long) row[1]) : 0;
            long totalSpent = row[2] != null ? ((BigDecimal) row[2]).longValue() : 0;
            long totalOrderAmount = row[3] != null ? ((BigDecimal) row[3]).longValue() : 0;
            long totalDebt = row[4] != null ? ((BigDecimal) row[4]).longValue() : 0;
            long totalQuantity = row[5] != null ? ((long) row[5]) : 0;
            customerData.add(new CustomerResponseDTO(customerName, totalOrder, totalSpent, totalQuantity, totalOrderAmount, totalDebt));
        }
        return customerData;
    }
}
