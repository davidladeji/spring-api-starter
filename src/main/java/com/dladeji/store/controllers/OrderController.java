package com.dladeji.store.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dladeji.store.dtos.OrderDto;
import com.dladeji.store.services.OrderService;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<Iterable<OrderDto>> getAllOrders() {
        var orders = orderService.getOrdersByUser();
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(
        @PathVariable Long orderId
    ) {
        var order = orderService.getOrder(orderId);
        return ResponseEntity.ok().body(order);
    }
    
}
