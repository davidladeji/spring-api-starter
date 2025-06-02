package com.dladeji.store.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dladeji.store.dtos.OrderCheckoutDto;
import com.dladeji.store.entities.Order;
import com.dladeji.store.exceptions.CartIsEmptyException;
import com.dladeji.store.repositories.OrderRepository;

@Service
public class CheckoutService {
    private CartService cartService;
    private AuthService authService;
    private OrderRepository orderRepository;
    
    public OrderCheckoutDto checkout(UUID cartId){
        var cart = cartService.getCartObj(cartId);

        if (cart.getItems().isEmpty())
            throw new CartIsEmptyException();
        var user = authService.getCurrentUser();

        
        var order = Order.fromCart(cart, user);
        orderRepository.save(order);
        cartService.clearCart(cartId);

        return new OrderCheckoutDto(order.getId());
    }
}
