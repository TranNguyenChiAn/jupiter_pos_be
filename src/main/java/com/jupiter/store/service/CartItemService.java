package com.jupiter.store.service;

import com.jupiter.store.model.CartItem;
import com.jupiter.store.model.ProductVariant;
import com.jupiter.store.dto.AddToCartDTO;
import com.jupiter.store.repository.CartItemRepository;
import com.jupiter.store.repository.ProductVariantRepository;
import com.jupiter.store.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    public CartItem addProductToCart(Long cartId, AddToCartDTO addToCartDTO) {
        Optional<ProductVariant> productVariant = productVariantRepository.findByVariantId(addToCartDTO.getProductVariantId());
        CartItem cartItem = new CartItem();
        if(productVariant.isPresent()) {
            cartItem.setCartId(cartId);
            cartItem.setProductVariantId(addToCartDTO.getProductVariantId());
            cartItem.setQuantity(addToCartDTO.getQuantity());
            cartItem.setTotalAmount(productVariant.get().getPrice() * addToCartDTO.getQuantity());
            cartItem.setCreatedBy(currentUserId());
            return cartItemRepository.save(cartItem);
        }else {
            return null;
        }
    }
    public CartItem updateCartItem(Long cartItemId, int quantity) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        Optional<ProductVariant> productVariant = productVariantRepository.findByVariantId(cartItem.get().getProductVariantId());
        if(cartItem.isPresent()) {
            cartItem.get().setQuantity(quantity);
            cartItem.get().setTotalAmount(productVariant.get().getPrice() * quantity);
            cartItem.get().setLastModifiedBy(currentUserId());
            return cartItemRepository.save(cartItem.get());
        }else {
            return null;
        }
    }
    public void deleteCartItem(Long cartId) {
        cartItemRepository.deleteAllByCartId(cartId);
    }
}