package com.jupiter.store.module.order.dto;

import com.jupiter.store.module.customer.dto.CustomerDTO;
import com.jupiter.store.module.order.constant.OrderStatus;
import com.jupiter.store.module.order.constant.OrderType;
import com.jupiter.store.module.order.model.Order;
import com.jupiter.store.module.payment.dto.PaymentReadDTO;
import com.jupiter.store.module.user.dto.UserReadDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderReadDTO {
    private Integer id;
    private UserReadDTO user;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Long totalAmount;
    private CustomerDTO customer;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String note;
    private OrderType orderType;
    private List<OrderDetailReadDTO> orderDetails;
    private List<PaymentReadDTO> payments;
    private List<OrderHistoryDTO> orderHistories;

    public OrderReadDTO(Order order) {
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getOrderStatus();
        this.totalAmount = order.getTotalAmount();
        this.receiverName = order.getReceiverName();
        this.receiverPhone = order.getReceiverPhone();
        this.receiverAddress = order.getReceiverAddress();
        this.note = order.getNote();
        this.orderType = order.getOrderType();
    }
}
