package com.jupiter.store.module.order.service;

import com.jupiter.store.module.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderHelperService {
    @Autowired
    private OrderRepository orderRepository;

    public void updateUserToNull(Integer userId) {
        orderRepository.updateUserToNull(userId);
    }
}