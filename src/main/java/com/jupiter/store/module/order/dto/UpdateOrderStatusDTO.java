package com.jupiter.store.module.order.dto;

import com.jupiter.store.module.order.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusDTO {
    private OrderStatus oldOrderStatus;
    private OrderStatus newOrderStatus;
}
