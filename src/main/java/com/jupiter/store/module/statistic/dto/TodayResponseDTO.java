package com.jupiter.store.module.statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodayResponseDTO {
    private long todayRevenue;
    private long todayProfit;
    private long thisMonthRevenue;
    private long last7DaysRevenue;

    private double todayChangePercent; // % tăng/giảm hôm qua
    private double thisMonthChangePercent; // % tăng/giảm tháng trước
    private long totalOrders; // Tổng số đơn hàng hôm nay
}

