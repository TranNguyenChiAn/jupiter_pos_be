package com.jupiter.store.module.statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponseDTO {
    private long todayRevenue;
    private long todayProfit;
    private long thisMonthRevenue;
    private long last7DaysRevenue;

    private Double todayChangePercent; // % tăng/giảm hôm qua
    private Double thisMonthChangePercent; // % tăng/giảm tháng trước
}

