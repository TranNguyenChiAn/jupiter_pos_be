package com.jupiter.store.module.order.dto;

import com.jupiter.store.module.order.model.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsDTO {
    private Integer orderId;
    private List<OrderDetail> orderItems;
} 