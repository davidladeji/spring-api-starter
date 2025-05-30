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
import com.dladeji.store.exceptions.OrderNotFoundException;
import com.dladeji.store.exceptions.UnauthorizedUserException;
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
    private AuthService authService;

    public OrderCheckoutDto checkout(UUID cartId){
        var cart = cartService.getCartObj(cartId);

        if (cart.getItems().isEmpty())
            throw new CartIsEmptyException();

        var user = authService.getCurrentUser();

        // Create Order
        var order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(cart.getTotalPrice());
        order.addItems(cart.getItems());
        // Save
        orderRepository.save(order);
        // Clear cart
        cartService.clearCart(cartId);

        return new OrderCheckoutDto(order.getId());
    }

    public List<OrderDto> getOrdersByUser(){
        // Get logged in user
        var user = authService.getCurrentUser();

        var orders = orderRepository.findByUser(user);
        List<OrderDto> userOrders = new ArrayList<>();
        orders.forEach(order -> {
            userOrders.add(orderMapper.toDto(order));
        });

        return userOrders;
    }

    public OrderDto getOrder(Long orderId){
        var user = authService.getCurrentUser();

        var order = orderRepository.findById(orderId).orElse(null);

        if (order == null)
            throw new OrderNotFoundException();

        if (order.getUser().getId() != user.getId())
            throw new UnauthorizedUserException();

        return orderMapper.toDto(order);
    }
}
