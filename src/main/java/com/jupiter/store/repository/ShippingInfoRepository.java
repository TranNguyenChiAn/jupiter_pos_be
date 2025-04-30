package com.jupiter.store.repository;

import com.jupiter.store.model.ShippingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShippingInfoRepository extends JpaRepository<ShippingInfo, Long> {
    @Query(value = "SELECT * FROM shipping_info s WHERE s.order_id = :orderId", nativeQuery = true)
    Optional<ShippingInfo> findByOrderId(@Param("orderId") Long orderId);
}
