package com.jupiter.store.module.statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InactiveCustomerDTO {
    private String customerId;
    private String customerName;
    private String phone;
    private String lastOrderDate;
    private int totalOrders;
    private Long totalSpent;
    private int daysSinceLastOrder;
}
