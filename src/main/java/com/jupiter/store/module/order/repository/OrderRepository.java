package com.jupiter.store.module.order.repository;

import com.jupiter.store.module.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query(value = "SELECT * FROM orders o WHERE o.user_id = :userId", nativeQuery = true)
    Page<Order> findByUserId(@Param("userId") Integer userId, Pageable pageable);


    @Query(
            value =
                    "  SELECT * FROM orders o " +
                            "  WHERE ( " +
                            "    (:search IS NULL OR unaccent(:search) = '') " +
                            "    OR (o.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                            "        OR o.receiver_phone ILIKE CONCAT('%', :search, '%')" +
                            "        OR LOWER(unaccent(o.receiver_name)) LIKE CONCAT('%', unaccent(:search), '%')" +
                            "        OR LOWER(unaccent(o.receiver_address)) LIKE CONCAT('%', unaccent(:search), '%')" +
                            "        OR LOWER(unaccent(o.note)) LIKE CONCAT('%', unaccent(:search), '%')" +
                            ") " +
                            "  ) " +
                            "  AND (o.order_date >= :startDate) " +
                            "  AND (o.order_date < :endDate) " +
                            "  AND (o.order_status IN :orderStatuses)",
            countQuery =
                    "SELECT COUNT(*) FROM orders o " +
                            "WHERE ( " +
                            "  (:search IS NULL OR unaccent(:search) = '') " +
                            "  OR (o.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                            "        OR o.receiver_phone ILIKE CONCAT('%', :search, '%')" +
                            "        OR LOWER(unaccent(o.receiver_name)) LIKE CONCAT('%', unaccent(:search), '%')" +
                            "        OR LOWER(unaccent(o.receiver_address)) LIKE CONCAT('%', unaccent(:search), '%')" +
                            "        OR LOWER(unaccent(o.note)) LIKE CONCAT('%', unaccent(:search), '%')" +
                            ") " +
                            ") " +
                            "  AND (o.order_date >= :startDate) " +
                            "  AND (o.order_date < :endDate) " +
                            " AND (o.order_status IN :orderStatuses) ",
            nativeQuery = true
    )
    Page<Order> search(
            @Param("search") String search,
            @Param("orderStatuses") List<String> orderStatuses,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE orders o SET user_id = NULL WHERE o.user_id = :userId", nativeQuery = true)
    void updateUserToNull(@Param("userId") Integer userId);
}
