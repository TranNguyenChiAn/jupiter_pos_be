package com.jupiter.store.module.order.service;

import com.jupiter.store.common.exception.CustomException;
import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.order.model.Order;
import com.jupiter.store.module.order.repository.OrderDetailRepository;
import com.jupiter.store.module.order.repository.OrderHistoryRepository;
import com.jupiter.store.module.order.repository.OrderRepository;
import com.jupiter.store.module.product.repository.ProductVariantRepository;
import com.jupiter.store.module.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private UserRepository userRepository;

    public static Long currentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    /**
     * Get order by ID
     */
    public ResponseEntity<Order> getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(order);
    }

    public ResponseEntity<List<Order>> getAllUserOrders() {
        List<Order> orders = orderRepository.findByUserId(currentUserId());
        return ResponseEntity.ok(orders);
    }
} 