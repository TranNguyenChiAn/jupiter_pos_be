package com.jupiter.store.module.order.repository;

import com.jupiter.store.module.order.constant.OrderStatus;
import com.jupiter.store.module.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query(value = "SELECT * FROM orders o WHERE o.user_id = :userId", nativeQuery = true)
    Page<Order> findByUserId(@Param("userId") Integer userId, Pageable pageable);


    @Query(
            value =
                    "WITH filtered_orders AS ( " +
                            "  SELECT o.*, " +
                            "         ts_rank_cd(o.fts, plainto_tsquery('simple', unaccent(:search))) AS rank " +
                            "  FROM orders o " +
                            "  WHERE ( " +
                            "    (:search IS NULL OR unaccent(:search) = '') " +
                            "    OR (o.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                            "        OR o.receiver_phone ILIKE CONCAT('%', :search, '%')) " +
                            "  ) " +
                            "  AND (o.order_date >= :startDate) " +
                            "  AND (o.order_date < :endDate) " +
                            "  AND (o.order_status IN :orderStatuses) " +
                            ") " +
                            "SELECT * FROM filtered_orders " +
                            "ORDER BY " +
                            "  CASE " +
                            "    WHEN (:search IS NULL OR unaccent(:search) = '') THEN NULL " +
                            "    ELSE COALESCE(rank, 0) " +
                            "  END DESC, " +
                            "  last_modified_date DESC NULLS LAST",

            countQuery =
                    "SELECT COUNT(*) FROM orders o " +
                            "WHERE ( " +
                            "  (:search IS NULL OR unaccent(:search) = '') " +
                            "  OR (o.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                            "      OR o.receiver_phone ILIKE CONCAT('%', :search, '%')) " +
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
}
