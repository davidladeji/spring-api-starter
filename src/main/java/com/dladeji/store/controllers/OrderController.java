package com.dladeji.store.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dladeji.store.dtos.CheckoutRequest;
import com.dladeji.store.dtos.OrderCheckoutDto;
import com.dladeji.store.dtos.OrderDto;
import com.dladeji.store.exceptions.CartIsEmptyException;
import com.dladeji.store.exceptions.CartNotFoundException;
import com.dladeji.store.services.OrderService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
    

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart not found.")
            );
    }

    @ExceptionHandler(CartIsEmptyException.class)
    public ResponseEntity<Map<String, String>> handleEmptyCart() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "Your cart is currently empty")
            );
    }
    
}
