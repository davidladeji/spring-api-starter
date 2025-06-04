package com.dladeji.store.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dladeji.store.carts.CartIsEmptyException;
import com.dladeji.store.carts.CartNotFoundException;
import com.dladeji.store.dtos.ErrorDto;
import com.dladeji.store.orders.OrderNotFoundException;
import com.dladeji.store.orders.UnauthorizedUserException;
import com.dladeji.store.products.ProductNotFoundException;
import com.dladeji.store.users.UserAlreadyExistsException;
import com.dladeji.store.users.UserNotFoundException;
import com.dladeji.store.users.WrongPasswordException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleInvalidArguments(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDto("Invalid Request Body")
            );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
        MethodArgumentNotValidException exception
    ) {
        var errors = new HashMap<String, String>();
        exception.getBindingResult().getFieldErrors().forEach(
            error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto("Cart not found.")
            );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDto("Product not found in the cart.")
            );
    }

    @ExceptionHandler(CartIsEmptyException.class)
    public ResponseEntity<ErrorDto> handleEmptyCart() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDto("Your cart is currently empty")
            );
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDto> handleOrderNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto("Order not found")
            );
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ErrorDto> handleUnauthorizedUser() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorDto("User not authorized to view this order")
            );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ErrorDto("User Not Found")
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleserAlreadyExistsException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ErrorDto("Email is already registered.")
        );
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ErrorDto> handleWrongPasswordException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorDto("Old password provided is invalid")
            );
    }
}
