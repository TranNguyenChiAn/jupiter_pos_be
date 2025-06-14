package com.jupiter.store.module.order.dto;

import com.jupiter.store.common.dto.AbstractAuditingDTO;
import com.jupiter.store.module.order.constant.OrderStatus;
import com.jupiter.store.module.order.model.OrderHistory;
import com.jupiter.store.module.user.dto.UserReadDTO;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryDTO extends AbstractAuditingDTO {
    private Integer id;
    private Integer orderId;
    private OrderStatus oldStatus;
    private OrderStatus newStatus;
    private UserReadDTO user;

    public OrderHistoryDTO(OrderHistory orderHistory) {
        this.id = orderHistory.getId();
        this.orderId = orderHistory.getOrderId();
        this.oldStatus = orderHistory.getOldStatus();
        this.newStatus = orderHistory.getNewStatus();
        this.setCreatedBy(orderHistory.getCreatedBy());
        this.setCreatedAt(orderHistory.getCreatedDate());
        this.setLastModifiedBy(orderHistory.getLastModifiedBy());
        this.setLastModifiedAt(orderHistory.getLastModifiedDate());
    }
}
