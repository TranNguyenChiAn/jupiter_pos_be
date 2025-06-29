package com.jupiter.store.module.statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusResponseDTO {
    private String orderStatus;
    private long totalOrders;
}
