package com.dladeji.store.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dladeji.store.dtos.CheckoutRequest;
import com.dladeji.store.dtos.ErrorDto;
import com.dladeji.store.dtos.OrderCheckoutDto;
import com.dladeji.store.exceptions.PaymentException;
import com.dladeji.store.services.CheckoutService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class CheckoutController {
    private CheckoutService checkoutService;
    
    @PostMapping("/checkout")
    public OrderCheckoutDto checkout(
        @Valid @RequestBody CheckoutRequest request
    ){
        
        return checkoutService.checkout(request.getCartId());
        
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorDto> handlePaymentException(){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating a checkout service"));
    }
}
