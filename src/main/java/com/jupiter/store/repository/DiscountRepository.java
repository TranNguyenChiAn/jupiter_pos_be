package com.jupiter.store.repository;

import com.jupiter.store.domain.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
