package com.jupiter.store.module.statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private String customerName;
    private long totalOrders;
    private long totalSpent;
    private long totalQuantity;
}
