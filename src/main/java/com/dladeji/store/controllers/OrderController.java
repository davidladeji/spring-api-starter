package com.dladeji.store.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dladeji.store.dtos.CheckoutRequest;
import com.dladeji.store.dtos.OrderCheckoutDto;
import com.dladeji.store.dtos.OrderDto;
import com.dladeji.store.services.OrderService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
public class OrderController {
    private OrderService orderService;
    
    @PostMapping("/checkout")
    public ResponseEntity<OrderCheckoutDto> checkout(
        @Valid @RequestBody CheckoutRequest request
    ){
        var orderDto = orderService.checkout(request.getCartId());
        return ResponseEntity.ok(orderDto);
    }

    @GetMapping("/orders")
    public ResponseEntity<Iterable<OrderDto>> getAllOrders() {
        var orders = orderService.getOrdersByUser();
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(
        @PathVariable Long orderId
    ) {
        var order = orderService.getOrder(orderId);
        return ResponseEntity.ok().body(order);
    }
    
}
