package com.jupiter.store.service;

import com.jupiter.store.dto.CartDTO;
import com.jupiter.store.model.Cart;
import com.jupiter.store.model.CartItem;
import com.jupiter.store.repository.CartItemRepository;
import com.jupiter.store.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    public Cart createCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setCreatedBy(userId);
        return cartRepository.save(cart);
    }
    public CartDTO getCart(Long userId) {
        CartDTO cartDTO = new CartDTO();
        Cart cart = cartRepository.findByUserId(userId);
        cartDTO.setId(cart.getId());
        cartDTO.setProductVariantId(cartItemRepository.findByCartId(cart.getId()));
        return cartDTO;
    }
}
