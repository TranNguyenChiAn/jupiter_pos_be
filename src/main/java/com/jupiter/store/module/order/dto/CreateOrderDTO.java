package com.jupiter.store.module.order.dto;

import com.jupiter.store.module.order.constant.OrderStatus;
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
public class CreateOrderDTO {
    private Integer customerId;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String note;
    private String paymentMethod;
    private List<OrderDetailCreateDTO> orderItems;
    private OrderStatus orderStatus = OrderStatus.CHO_XAC_NHAN;
}
