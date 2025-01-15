package com.jupiter.store.service;

import com.jupiter.store.domain.Cart;
import com.jupiter.store.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public Cart createCart(Long customerId) {
        Cart cart = new Cart();
        cart.setCustomerId(customerId);
        cart.setCreatedBy(customerId);
        return cartRepository.save(cart);
    }

}
