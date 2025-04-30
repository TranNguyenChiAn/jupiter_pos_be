package com.jupiter.store.service;

import com.jupiter.store.model.CartItem;
import com.jupiter.store.model.ProductVariant;
import com.jupiter.store.dto.AddToCartDTO;
import com.jupiter.store.repository.CartItemRepository;
import com.jupiter.store.repository.ProductVariantRepository;
import com.jupiter.store.utils.SecurityUtils;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemService {
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    public static Long currentUserId(){
        return SecurityUtils.getCurrentUserId();
    }

    public ResponseEntity<CartItem> addProductToCart(Long cartId, Long productVariantId) {
        ProductVariant productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Product variant not found"));
        CartItem cartItem = new CartItem();
        cartItem.setCartId(cartId);
        cartItem.setProductVariantId(productVariantId);
        cartItem.setQuantity(1);
        cartItem.setTotalAmount(productVariant.getPrice());
        cartItem.setCreatedBy(currentUserId());
        return ResponseEntity.ok(cartItemRepository.save(cartItem));

    }
    public ResponseEntity<CartItem> updateCartItem(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new OpenApiResourceNotFoundException("Cart item not found"));
        ProductVariant productVariant = productVariantRepository.findById(cartItem.getProductVariantId())
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Product variant not found"));

            cartItem.setQuantity(quantity);
            cartItem.setTotalAmount(productVariant.getPrice() * quantity);
            cartItem.setLastModifiedBy(currentUserId());
            return ResponseEntity.ok(cartItemRepository.save(cartItem));

    }
    public void deleteCartItem(Long cartId) {
        cartItemRepository.deleteAllByCartId(cartId);
    }
}