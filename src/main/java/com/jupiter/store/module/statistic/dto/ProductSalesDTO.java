package com.jupiter.store.module.statistic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSalesDTO {
    private String productName;
    private Long totalQuantity;
    private Long revenue;
}
