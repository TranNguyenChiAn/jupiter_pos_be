package com.jupiter.store.module.order.dto;

import com.jupiter.store.module.order.constant.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDTO {
    private Integer customerId;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String note;
    private OrderType orderType;
}
