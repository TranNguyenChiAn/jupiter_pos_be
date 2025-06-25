package com.jupiter.store.module.payment.repository;

import com.jupiter.store.module.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @Query(value = "SELECT * FROM payments p WHERE p.order_id = :orderId order by p.id ASC", nativeQuery = true)
    List<Payment> findByOrderId(@Param("orderId") Integer orderId);

}
