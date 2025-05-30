package com.dladeji.store.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dladeji.store.dtos.OrderCheckoutDto;
import com.dladeji.store.dtos.OrderDto;
import com.dladeji.store.entities.Order;
import com.dladeji.store.entities.OrderStatus;
import com.dladeji.store.exceptions.CartIsEmptyException;
import com.dladeji.store.mappers.OrderMapper;
import com.dladeji.store.repositories.OrderRepository;
import com.dladeji.store.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderService {
    private CartService cartService;
    private OrderRepository orderRepository;
    private OrderMapper orderMapper;
    private UserRepository userRepository;

    public OrderCheckoutDto checkout(UUID cartId){
        var cart = cartService.getCartObj(cartId);

        if (cart.getItems().isEmpty())
            throw new CartIsEmptyException();

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();
        var user = userRepository.findById(userId).orElseThrow();

        // Need to create an order
        var order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(cart.getTotalPrice());
        order.addItems(cart.getItems());
        // Save it
        orderRepository.save(order);
        // Clear the cart
        cartService.clearCart(cartId);

        return new OrderCheckoutDto(order.getId());
    }

    public List<OrderDto> getOrdersByUser(){
        // Get logged in user
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();
        // var user = userRepository.findById(userId).orElseThrow();

        var orders = orderRepository.findByUserId(userId);
        List<OrderDto> userOrders = new ArrayList<>();
        orders.forEach(order -> {
            userOrders.add(orderMapper.toDto(order));
        });

        return userOrders;
    }
}
