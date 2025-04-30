package com.jupiter.store.repository;

import com.jupiter.store.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query(value = "DELETE FROM cart_items WHERE cart_id = :cartId", nativeQuery = true)
    void deleteAllByCartId(@Param("cartId") Long cartId);
    @Query(value = "SELECT * FROM cart_items WHERE cart_id = :cartId", nativeQuery = true)
    List<CartItem> findByCartId(@Param("cartId") Long cartId);
}