package com.jupiter.store.service;

import com.jupiter.store.dto.cart.CartDTO;
import com.jupiter.store.dto.cart.CartItemDTO;
import com.jupiter.store.model.Cart;
import com.jupiter.store.model.CartItem;
import com.jupiter.store.model.Product;
import com.jupiter.store.model.ProductVariant;
import com.jupiter.store.repository.CartItemRepository;
import com.jupiter.store.repository.CartRepository;
import com.jupiter.store.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    public Cart createCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setCreatedBy(userId);
        return cartRepository.save(cart);
    }

    public CartDTO getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        List<CartItem> cartItems= cartItemRepository.findByCartId(cart.getId());

        List<CartItemDTO> cartItemDTOS = cartItems.stream()
                .map(cartItem -> {
                    ProductVariant productVariant = productVariantRepository.findById(cartItem.getProductVariantId())
                            .orElseThrow(() -> new RuntimeException("Product variant not found"));
                    CartItemDTO cartItemDTO = new CartItemDTO();
                    cartItemDTO.setId(cartItem.getId());
                    cartItemDTO.setQuantity(cartItem.getQuantity());
                    cartItemDTO.setTotalAmount(cartItem.getTotalAmount());
                    return cartItemDTO;
                })
                .collect(Collectors.toList());

        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setCartItems(cartItemDTOS);
        return cartDTO;
    }
}
