package com.jupiter.store.module.statistic.service;

import com.jupiter.store.module.statistic.dto.DashboardResponseDTO;
import com.jupiter.store.module.statistic.dto.ProductSalesDTO;
import com.jupiter.store.module.statistic.repository.ProductStatisticRepository;
import com.jupiter.store.module.statistic.repository.RevenueStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RevenueService {
    @Autowired
    private RevenueStatisticRepository revenueStatisticRepository;

    @Autowired
    private ProductStatisticRepository productStatisticRepository;

    public DashboardResponseDTO getDashboardData() {
        Object result = revenueStatisticRepository.getMainStats();
        Object[] row = (Object[]) result;
        long today = row[0] != null ? ((BigDecimal) row[0]).longValue() : 0L;
        long todayProfit = row[1] != null ? ((BigDecimal) row[1]).longValue() : 0L;
        long yesterday = row[2] != null ? ((BigDecimal) row[2]).longValue() : 0L;
        long thisMonth = row[3] != null ? ((BigDecimal) row[3]).longValue() : 0L;
        long lastMonth = row[4] != null ? ((BigDecimal) row[4]).longValue() : 0L;
        long last7Days = row[5] != null ? ((BigDecimal) row[5]).longValue() : 0L;

        Double todayChange = (yesterday == 0) ? null :
                ((today - (double) yesterday) / yesterday) * 100;

        Double monthChange = (lastMonth == 0) ? null :
                ((thisMonth - (double) lastMonth) / lastMonth) * 100;

        return new DashboardResponseDTO(
                today,
                todayProfit,
                thisMonth,
                last7Days,
                todayChange,
                monthChange
        );
    }

    public List<ProductSalesDTO> getProductData(
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        return productStatisticRepository.findTopSellingProducts(startTime, endTime);
    }
}
