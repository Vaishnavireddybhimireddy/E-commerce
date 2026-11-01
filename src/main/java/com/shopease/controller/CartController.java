package com.shopease.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopease.entity.CartItem;
import com.shopease.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Add product to cart
    @PostMapping("/add/{productId}")
    public CartItem addToCart(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            Authentication authentication) {

        return cartService.addToCart(
                authentication.getName(), productId, quantity);
    }

    // View cart
    @GetMapping
    public List<CartItem> getCart(Authentication authentication) {
        return cartService.getCart(authentication.getName());
    }

    // Update quantity
    @PutMapping("/update/{cartItemId}")
    public CartItem updateQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity,
            Authentication authentication) {

        return cartService.updateQuantity(
                authentication.getName(), cartItemId, quantity);
    }

    // Remove one item
    @DeleteMapping("/remove/{cartItemId}")
    public String removeFromCart(
            @PathVariable Long cartItemId,
            Authentication authentication) {

        cartService.removeFromCart(
                authentication.getName(), cartItemId);

        return "Item removed from cart successfully";
    }

    // Clear cart
    @DeleteMapping("/clear")
    public String clearCart(Authentication authentication) {

        cartService.clearCart(authentication.getName());

        return "Cart cleared successfully";
    }
}