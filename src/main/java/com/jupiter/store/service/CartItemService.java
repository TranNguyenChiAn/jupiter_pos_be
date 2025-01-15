package com.jupiter.store.service;

import com.jupiter.store.domain.CartItem;
import com.jupiter.store.domain.ProductVariant;
import com.jupiter.store.dto.AddToCartDTO;
import com.jupiter.store.repository.CartItemRepository;
import com.jupiter.store.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {
    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartItem addProductToCart(Long cartId, AddToCartDTO addToCartDTO) {
        Optional<ProductVariant> productVariant = productVariantRepository.findByVariantId(addToCartDTO.getProductVariantId());
        CartItem cartItem = new CartItem();
        if(productVariant.isPresent()) {
            cartItem.setCartId(cartId);
            cartItem.setProductVariantId(addToCartDTO.getProductVariantId());
            cartItem.setQuantity(addToCartDTO.getQuantity());
            cartItem.setTotalAmount(productVariant.get().getPrice() * addToCartDTO.getQuantity());
            cartItem.setCreatedBy(5191077281046528L);
            return cartItemRepository.save(cartItem);
        }else {
            return null;
        }
    }
}
