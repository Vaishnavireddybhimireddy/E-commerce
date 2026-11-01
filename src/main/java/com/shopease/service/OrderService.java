package com.shopease.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopease.entity.AppUser;
import com.shopease.entity.CartItem;
import com.shopease.entity.CustomerOrder;
import com.shopease.entity.OrderItem;
import com.shopease.repository.CartRepository;
import com.shopease.repository.OrderRepository;
import com.shopease.repository.UserRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    private AppUser getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new RuntimeException("User not found"));
    }

    @Transactional
    public CustomerOrder placeOrder(String username) {

        AppUser user = getUser(username);

        List<CartItem> cartItems = cartRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Your cart is empty");
        }

        CustomerOrder order = new CustomerOrder();
        order.setUser(user);

        double total = 0;

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            double price = cartItem.getProduct()
                    .getPrice().doubleValue();

            OrderItem orderItem = new OrderItem(
                    order,
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    price
            );

            orderItems.add(orderItem);

            total += price * cartItem.getQuantity();
        }

        order.getItems().addAll(orderItems);
        order.setTotalAmount(total);

        CustomerOrder savedOrder = orderRepository.save(order);

        cartRepository.deleteByUser(user);

        return savedOrder;
    }

    public List<CustomerOrder> getMyOrders(String username) {

        AppUser user = getUser(username);

        return orderRepository.findByUser(user);
    }

    public CustomerOrder getOrderById(
            String username, Long orderId) {

        AppUser user = getUser(username);

        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                    new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You cannot access this order");
        }

        return order;
    }
}