package com.jupiter.store.repository;

import com.jupiter.store.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query(value = "SELECT * FROM carts c WHERE c.user_id = :userId", nativeQuery = true)
    Cart findByUserId(@RequestParam("userId") Long userId);
}
