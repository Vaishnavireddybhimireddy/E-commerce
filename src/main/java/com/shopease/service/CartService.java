package com.shopease.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopease.entity.AppUser;
import com.shopease.entity.CartItem;
import com.shopease.entity.Product;
import com.shopease.repository.CartRepository;
import com.shopease.repository.ProductRepository;
import com.shopease.repository.UserRepository;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private AppUser getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new RuntimeException("User not found"));
    }

    public CartItem addToCart(String username, Long productId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                "Quantity must be greater than zero");
        }

        AppUser user = getUser(username);

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                    new RuntimeException("Product not found"));

        CartItem item = cartRepository
                .findByUserAndProductId(user, productId)
                .orElse(new CartItem(user, product, 0));

        item.setQuantity(item.getQuantity() + quantity);

        return cartRepository.save(item);
    }

    public List<CartItem> getCart(String username) {
        AppUser user = getUser(username);
        return cartRepository.findByUser(user);
    }

    public CartItem updateQuantity(
            String username, Long cartItemId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                "Quantity must be greater than zero");
        }

        AppUser user = getUser(username);

        CartItem item = cartRepository.findById(cartItemId)
                .orElseThrow(() ->
                    new RuntimeException("Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Cart item does not belong to you");
        }

        item.setQuantity(quantity);

        return cartRepository.save(item);
    }

    public void removeFromCart(String username, Long cartItemId) {

        AppUser user = getUser(username);

        CartItem item = cartRepository.findById(cartItemId)
                .orElseThrow(() ->
                    new RuntimeException("Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Cart item does not belong to you");
        }

        cartRepository.delete(item);
    }

    public void clearCart(String username) {
        AppUser user = getUser(username);
        cartRepository.deleteByUser(user);
    }
}