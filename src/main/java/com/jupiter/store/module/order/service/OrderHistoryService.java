package com.jupiter.store.module.order.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.order.constant.OrderStatus;
import com.jupiter.store.module.order.dto.*;
import com.jupiter.store.module.order.model.OrderHistory;
import com.jupiter.store.module.order.repository.OrderHistoryRepository;
import com.jupiter.store.module.user.dto.UserReadDTO;
import com.jupiter.store.module.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderHistoryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    public static Integer currentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    public void createOrderHistory(Integer orderId, OrderStatus oldStatus, OrderStatus newStatus) {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrderId(orderId);
        orderHistory.setOldStatus(oldStatus);
        orderHistory.setNewStatus(newStatus);
        orderHistory.setCreatedBy(currentUserId());
        orderHistoryRepository.save(orderHistory);
    }

    public List<OrderHistoryDTO> getOrderHistoriesByOrderId(Integer orderId) {
        List<OrderHistory> orderHistories = orderHistoryRepository.findAllByOrderId(orderId);
        return orderHistories.stream().map(oh -> {
            OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO(oh);
            userRepository.findById(oh.getCreatedBy()).ifPresent(user -> orderHistoryDTO.setUser(new UserReadDTO(user)));
            return orderHistoryDTO;
        }).collect(Collectors.toList());
    }
}