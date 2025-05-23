package com.jupiter.store.module.customer.repository;

import com.jupiter.store.module.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query(value = "SELECT * FROM customers c WHERE\n" +
            "       LOWER(c.full_name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR\n" +
            "       LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR\n" +
            "       LOWER(c.phone_number) LIKE LOWER(CONCAT('%', :keyword, '%')) OR\n" +
            "       LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)
    Optional<Customer> findByKeyword(@Param("keyword") String keyword);
}
