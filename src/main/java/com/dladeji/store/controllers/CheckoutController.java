package com.dladeji.store.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dladeji.store.dtos.CheckoutRequest;
import com.dladeji.store.dtos.OrderCheckoutDto;
import com.dladeji.store.services.CheckoutService;

import jakarta.validation.Valid;

@RestController
public class CheckoutController {
    private CheckoutService checkoutService;
    
    @PostMapping("/checkout")
    public ResponseEntity<OrderCheckoutDto> checkout(
        @Valid @RequestBody CheckoutRequest request
    ){
        var orderDto = checkoutService.checkout(request.getCartId());
        return ResponseEntity.ok(orderDto);
    }
}
