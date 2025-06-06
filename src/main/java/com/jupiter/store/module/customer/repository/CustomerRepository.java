package com.jupiter.store.module.customer.repository;

import com.jupiter.store.module.customer.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query(value = "SELECT * FROM customers c WHERE " +
            "       LOWER(c.full_name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "       LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "       LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "       LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)
    Optional<Customer> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM customers WHERE phone = :phone", nativeQuery = true)
    Customer findByPhone(@Param("phone") String phone);

    @Query(
            value = "SELECT * FROM ( " +
                    "  SELECT c.*, " +
                    "         ts_rank_cd(c.fts, plainto_tsquery('simple', unaccent(:search))) AS rank " +
                    "  FROM customers c " +
                    "  WHERE (:search IS NULL OR unaccent(:search) = '' " +
                    "         OR c.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                    "         OR c.phone LIKE CONCAT('%', :search, '%') " +
                    "        ) " +
                    ") sub " +
                    "ORDER BY " +
                    "  CASE " +
                    "    WHEN (:search IS NULL OR unaccent(:search) = '') THEN NULL " +
                    "    ELSE COALESCE(sub.rank, 0) " +
                    "  END DESC, " +
                    "  sub.last_modified_date DESC nulls last",
            countQuery = "SELECT COUNT(*) FROM customers c " +
                    "WHERE (:search IS NULL OR unaccent(:search) = '' " +
                    "       OR c.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                    "         OR c.phone LIKE CONCAT('%', :search, '%') " +
                    "      )",
            nativeQuery = true)
    Page<Customer> search(@Param("search") String search, Pageable pageable);
}
