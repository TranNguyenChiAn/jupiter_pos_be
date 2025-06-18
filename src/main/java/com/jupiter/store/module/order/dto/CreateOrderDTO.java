package com.jupiter.store.module.order.dto;

import com.jupiter.store.module.order.constant.OrderStatus;
import com.jupiter.store.module.order.constant.OrderType;
import com.jupiter.store.module.order.model.OrderDetail;
import com.jupiter.store.module.payment.constant.PaymentMethod;
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
    private Long paid;
    private PaymentMethod paymentMethod;
    private List<OrderDetailCreateDTO> orderItems;
    private OrderType orderType = OrderType.MUA_TRUC_TIEP;
}
