package com.jupiter.store.module.order.repository;

import com.jupiter.store.module.order.model.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Integer> {

    OrderHistory findByOrderId(Integer orderId);

    List<OrderHistory> findAllByOrderId(Integer orderId);
}