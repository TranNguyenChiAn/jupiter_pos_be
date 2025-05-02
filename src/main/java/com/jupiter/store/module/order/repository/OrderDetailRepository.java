package com.jupiter.store.module.order.repository;

import com.jupiter.store.module.order.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);

    @Query(value = "SELECT * FROM order_details od WHERE od.order_id = :orderId AND od.product_variant_id = :productVariantId", nativeQuery = true)
    OrderDetail findByOrderIdAndProductVariantId(Long orderId, Long productVariantId);
}