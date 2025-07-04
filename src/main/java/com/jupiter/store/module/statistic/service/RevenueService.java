package com.jupiter.store.module.statistic.service;

import com.jupiter.store.common.utils.HelperUtils;
import com.jupiter.store.module.payment.constant.PaymentMethod;
import com.jupiter.store.module.product.repository.ProductRepository;
import com.jupiter.store.module.product.repository.ProductVariantAttrValueRepository;
import com.jupiter.store.module.statistic.dto.*;
import com.jupiter.store.module.statistic.repository.CustomerStatisticRepository;
import com.jupiter.store.module.statistic.repository.OrderStatisticRepository;
import com.jupiter.store.module.statistic.repository.ProductStatisticRepository;
import com.jupiter.store.module.statistic.repository.RevenueStatisticRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
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
    @Autowired
    private OrderStatisticRepository orderStatisticRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private HelperUtils helperUtils;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductVariantAttrValueRepository productVariantAttrValueRepository;

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
        String labelExpr;
        String groupByExpr;
        String sortTimeExpr;

        switch (groupByLabel.toLowerCase()) {
            case "day" -> {
                labelExpr = "TO_CHAR(p.date, 'DD/MM')";
                groupByExpr = labelExpr;
                sortTimeExpr = "MIN(DATE_TRUNC('day', p.date))";
            }
            case "hour" -> {
                labelExpr = "TO_CHAR(DATE_TRUNC('hour', p.date), 'HH24:00')";
                groupByExpr = labelExpr;
                sortTimeExpr = "MIN(DATE_TRUNC('hour', p.date))";
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
                groupByExpr = labelExpr;
                sortTimeExpr = "MIN(EXTRACT(DOW FROM p.date))";
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
                """, labelExpr, sortTimeExpr, groupByExpr);

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);

        List<Object[]> results = query.getResultList();
        for (Object[] row : results) {
            String label = row[0] != null ? row[0].toString() : "";
            long revenue = row[1] != null ? ((BigDecimal) row[1]).longValue() : 0L;
            netRevenueData.add(new NetRevenueDTO(label, revenue));
        }

        return netRevenueData;
    }

    public List<ProductSalesDTO> getProductData(
            String sortOrder,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        if(sortOrder == null || sortOrder.isBlank()) {
            sortOrder = "DESC";
        }

        String sql = String.format("""
        WITH variant_attrs AS (
            SELECT
                pav.product_variant_id,
                STRING_AGG(pav.attr_value, ', ') AS attr_values
            FROM product_attribute_values pav
            GROUP BY pav.product_variant_id
        )
        SELECT
            p.product_name ||
            CASE
                WHEN va.attr_values IS NOT NULL THEN ' - ' || va.attr_values
                ELSE ''
            END AS product_name,
            SUM(od.sold_quantity) AS total_quantity_sold,
            SUM(od.sold_price * od.sold_quantity) AS total_revenue
        FROM product_variants pv
        INNER JOIN order_details od ON pv.id = od.product_variant_id
        INNER JOIN products p ON p.id = pv.product_id
        LEFT JOIN variant_attrs va ON va.product_variant_id = pv.id
        INNER JOIN orders o ON o.id = od.order_id
        WHERE o.order_date BETWEEN :startTime AND :endTime
        GROUP BY pv.id, p.product_name, va.attr_values
        ORDER BY total_revenue %s
        LIMIT 10
        """, sortOrder);

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);

        List<Object[]> results = query.getResultList();
        if (results.isEmpty()) {
            return new ArrayList<>();
        }
        List<ProductSalesDTO> productSales = new ArrayList<>();
        for (Object[] row : results) {
            String productName = row[0] != null ? ((String) row[0]) : "";
            long totalQuantity = row[1] != null ? ((Long) row[1]) : 0L;
            long revenue = row[2] != null ? ((BigDecimal) row[2]).longValue() : 0L;
            productSales.add(new ProductSalesDTO(productName, totalQuantity, revenue));
        }
        return productSales;
    }

    public List<DeadStockProductDTO> getDeadStockData() {
        List<DeadStockProductDTO> deadStockData = new ArrayList<>();
        List<Object[]> results = productStatisticRepository.getDeadStockData();
        for (Object[] row : results) {
            String productName = row[0] != null ? ((String) row[0]) : "";
            int dayCount = row[1] != null ? ((Integer) row[1]) : 0;
            deadStockData.add(new DeadStockProductDTO(productName, dayCount));
        }

        return deadStockData;
    }

    public List<ProductInventoryDTO> getLowInventoryProduct() {
        List<ProductInventoryDTO> productInventoryData = new ArrayList<>();
        List<Object[]> results = productStatisticRepository.getLowInventoryProduct();

        for (Object[] row : results) {
            String productName = row[0] != null ? ((String)row[0]) : "";
            int inventoryCount = row[1] != null ? ((Integer) row[1]) : 0;
            productInventoryData.add(new ProductInventoryDTO(productName, inventoryCount));
        }

        return productInventoryData;
    }

    public List<CustomerResponseDTO> getCustomersData(
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        List<CustomerResponseDTO> customerData = new ArrayList<>();
        List<Object[]> results = customerStatisticRepository.getCustomerData(startTime, endTime);
        for (Object[] row : results) {
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

    public List<InactiveCustomerDTO> getInactiveCustomers(String sortBy, String sortDirection) {
        sortBy = helperUtils.normalizeSort(sortBy);
        sortDirection = helperUtils.normalizeSortDirection(sortDirection);

        List<InactiveCustomerDTO> inactiveCustomers = new ArrayList<>();

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "DAYS_SINCE_LAST_ORDER";
        }
        String sortExpr = sortBy + " " + sortDirection;

        String sql = String.format("""
                SELECT
                    C.ID,
                    C.CUSTOMER_NAME,
                    C.PHONE,
                    MAX(O.CREATED_DATE) AS LAST_ORDER_DATE,
                    C.TOTAL_ORDERS,
                    C.TOTAL_SPENT,
                    CAST(
                        DATE_PART('day', CURRENT_TIMESTAMP - MAX(O.CREATED_DATE)) AS INTEGER
                    ) AS DAYS_SINCE_LAST_ORDER
                FROM
                    CUSTOMERS C
                LEFT JOIN
                    ORDERS O ON O.CUSTOMER_ID = C.ID
                GROUP BY
                    C.ID, C.CUSTOMER_NAME, C.PHONE
                HAVING
                	MAX(O.CREATED_DATE) IS NOT NULL
                ORDER BY
                    %s NULLS LAST
                LIMIT 10;
                """, sortExpr);

        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> results = query.getResultList();

        for (Object[] row : results) {
            String customerId = row[0] != null ? row[0].toString() : "";
            String customerName = row[1] != null ? row[1].toString() : "";
            String phone = row[2] != null ? row[2].toString() : "";
            String lastOrderDate = row[3] != null ? row[3].toString() : "";
            int totalOrders = row[4] != null ? ((Integer) row[4]) : 0;
            Long totalSpent = row[5] != null ? ((Long) row[5]) : 0L;
            int daysSinceLastOrder = row[6] != null ? ((Integer) row[6]) : 0;

            inactiveCustomers.add(new InactiveCustomerDTO(customerId, customerName, phone, lastOrderDate, totalOrders, totalSpent, daysSinceLastOrder));
        }

        return inactiveCustomers;
    }

    public List<NewCustomerDTO> getNewCustomers(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.of(1970, 1, 1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        List<NewCustomerDTO> newCustomers = new ArrayList<>();
        String sql = """
                SELECT
                	COUNT(*) AS CUSTOMER_COUNT,
                	CAST(DATE_TRUNC('day', CREATED_DATE) AS DATE) AS DATE
                FROM
                	CUSTOMERS
                WHERE
                	CAST(CREATED_DATE AS DATE) BETWEEN :startDate AND :endDate
                GROUP BY
                	CAST(DATE_TRUNC('day', CREATED_DATE) AS DATE)
                ORDER BY
                	CAST(DATE_TRUNC('day', CREATED_DATE) AS DATE);
                """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        List<Object[]> results = query.getResultList();
        for (Object[] row : results) {
            Integer customerCount = row[0] != null ? ((Long) row[0]).intValue() : 0;
            LocalDate date = row[1] != null ? ((Date) row[1]).toLocalDate() : LocalDate.now();
            newCustomers.add(new NewCustomerDTO(customerCount, date));
        }

        return newCustomers;
    }

    public List<OrderStatusResponseDTO> getOrderStatusStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        List<OrderStatusResponseDTO> orderStatusStatistics = new ArrayList<>();
        List<Object[]> result = orderStatisticRepository.getOrderStatusStatistics(startTime, endTime);
        for (Object[] row : result) {
            String orderStatus = row[0] != null ? ((String) row[0]) : "";
            long totalOrders = row[1] != null ? ((long) row[1]) : 0L;
            orderStatusStatistics.add(new OrderStatusResponseDTO(orderStatus, totalOrders));
        }

        return orderStatusStatistics;
    }

    public List<PaymentMethodStatisticDTO> getPaymentMethodsData(LocalDate startDate, LocalDate endDate) {
        List<PaymentMethodStatisticDTO> paymentMethodsData = new ArrayList<>();
        List<Object[]> results = revenueStatisticRepository.getPaymentMethodsData(startDate, endDate);

        for (Object[] row : results) {
            PaymentMethod paymentMethod = row[0] != null ? PaymentMethod.fromString((String) row[0]) : null;
            Integer totalOrders = row[1] != null ? ((Integer) row[1]) : 0;
            Long totalRevenue = row[2] != null ? ((Long) row[2]) : 0L;
            paymentMethodsData.add(new PaymentMethodStatisticDTO(paymentMethod, totalOrders, totalRevenue));
        }

        return paymentMethodsData;
    }

    public RevenueDTO getRevenueByDate(LocalDate startDate, LocalDate endDate) {
        RevenueDTO revenueData = new RevenueDTO(0L, 0);
        List<Object[]> results = revenueStatisticRepository.getRevenueByDate(startDate, endDate);

        for (Object[] row : results) {
            Long totalRevenue = row[0] != null ? (Long) row[0] : 0L;
            Integer totalOrders = row[1] != null ? (Integer) row[1] : 0;
            revenueData = new RevenueDTO(totalRevenue, totalOrders);
        }
        return revenueData;
    }
}
