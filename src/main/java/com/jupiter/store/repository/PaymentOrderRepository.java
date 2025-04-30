package com.jupiter.store.repository;

import com.jupiter.store.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    @Query(value = "SELECT * FROM payment_orders p WHERE p.order_id = :orderId", nativeQuery = true)
    Optional<PaymentOrder> findByOrderId(@Param("orderId") Long orderId);
}
