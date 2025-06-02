package com.dladeji.store.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dladeji.store.dtos.OrderDto;
import com.dladeji.store.exceptions.OrderNotFoundException;
import com.dladeji.store.exceptions.UnauthorizedUserException;
import com.dladeji.store.mappers.OrderMapper;
import com.dladeji.store.repositories.OrderRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private OrderMapper orderMapper;
    private AuthService authService;

    public List<OrderDto> getOrdersByUser(){
        var user = authService.getCurrentUser();

        var orders = orderRepository.getAllByUser(user);
        List<OrderDto> userOrders = new ArrayList<>();
        orders.forEach(order -> {
            userOrders.add(orderMapper.toDto(order));
        });

        return userOrders;
    }

    public OrderDto getOrder(Long orderId){
        var user = authService.getCurrentUser();
        var order = orderRepository.getOrderWithItems(orderId).orElse(null);

        if (order == null)
            throw new OrderNotFoundException();

        if (!order.isPlacedBy(user))
            throw new UnauthorizedUserException();

        return orderMapper.toDto(order);
    }
}
