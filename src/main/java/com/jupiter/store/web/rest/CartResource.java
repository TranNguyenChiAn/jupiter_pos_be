package com.jupiter.store.web.rest;

import com.jupiter.store.dto.cart.CartDTO;
import com.jupiter.store.model.Cart;
import com.jupiter.store.model.CartItem;
import com.jupiter.store.dto.AddToCartDTO;
import com.jupiter.store.service.CartItemService;
import com.jupiter.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartResource {
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/get-cart")
    public ResponseEntity<CartDTO> getCart(Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/create-cart")
    public ResponseEntity<Cart> createCart(Long userId) {
        Cart cart = cartService.createCart(userId);
        return ResponseEntity.ok(cart);
    }
    @PostMapping("/add-cart-items/{cartId}/{productVariantId}")
    public ResponseEntity<CartItem> addProductToCart(@PathVariable Long cartId, @PathVariable Long productVariantId) {
        ResponseEntity<CartItem> cartItem = cartItemService.addProductToCart(cartId, productVariantId);
        return ResponseEntity.ok(cartItem.getBody());
    }
    @PutMapping("/update-cart-item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long cartItemId, @RequestParam int quantity) {
        ResponseEntity<CartItem> updatedCartItem = cartItemService.updateCartItem(cartItemId, quantity);
        return ResponseEntity.ok(updatedCartItem.getBody());
    }
    @DeleteMapping("/delete-cart-item/{cartId}")
    public void deleteCartItem(@PathVariable Long cartId) {
        cartItemService.deleteCartItem(cartId);
    }
}