package com.shopease.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopease.entity.CustomerOrder;
import com.shopease.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Place an order
    @PostMapping("/place")
    public CustomerOrder placeOrder(Authentication authentication) {

        return orderService.placeOrder(authentication.getName());
    }

    // View all orders of logged-in user
    @GetMapping
    public List<CustomerOrder> getMyOrders(Authentication authentication) {

        return orderService.getMyOrders(authentication.getName());
    }

    // View a particular order
    @GetMapping("/{orderId}")
    public CustomerOrder getOrderById(
            @PathVariable Long orderId,
            Authentication authentication) {

        return orderService.getOrderById(
                authentication.getName(), orderId);
    }
}