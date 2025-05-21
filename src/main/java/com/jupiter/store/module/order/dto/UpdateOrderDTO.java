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
public class UpdateOrderDTO {
    private Integer orderId;
    private Integer customerId;
    private OrderStatus orderStatus;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private double taxRate;
    private String note;
}
