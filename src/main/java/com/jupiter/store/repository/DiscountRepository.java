package com.jupiter.store.repository;

import com.jupiter.store.domain.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    @Query(value = "SELECT * FROM discount WHERE product_id = :productId", nativeQuery = true)
    Optional<Discount> findByProductId(@Param("productId") Long productId);
}
