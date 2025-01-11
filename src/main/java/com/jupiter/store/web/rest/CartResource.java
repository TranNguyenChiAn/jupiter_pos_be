package com.jupiter.store.web.rest;

import com.jupiter.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartResource {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public void addProductToCart(Long productId, Long quantity) {
        cartService.addProductToCart(productId, quantity);
    }
}
