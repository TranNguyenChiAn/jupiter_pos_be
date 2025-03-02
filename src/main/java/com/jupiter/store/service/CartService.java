package com.jupiter.store.service;

import com.jupiter.store.domain.Cart;
import com.jupiter.store.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public Cart createCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setCreatedBy(userId);
        return cartRepository.save(cart);
    }

}
