package com.jupiter.store.module.payment.repository;

import com.jupiter.store.module.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    // Define any custom query methods if needed
}
