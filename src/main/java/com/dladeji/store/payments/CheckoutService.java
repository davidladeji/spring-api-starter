package com.dladeji.store.payments;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dladeji.store.auth.AuthService;
import com.dladeji.store.entities.Order;
import com.dladeji.store.exceptions.CartIsEmptyException;
import com.dladeji.store.repositories.OrderRepository;
import com.dladeji.store.services.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartService cartService;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;
    
    @Transactional
    public CheckoutResponse checkout(UUID cartId) throws PaymentException{
        var cart = cartService.getCartObj(cartId);

        if (cart.getItems().isEmpty())
            throw new CartIsEmptyException();
        var user = authService.getCurrentUser();

        
        var order = Order.fromCart(cart, user);
        orderRepository.save(order);

        try {
            // Create a checkout session
            var session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cartId);

            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());

        } catch (PaymentException ex){
            orderRepository.delete(order);
            throw ex;
        }
    }

    public void handleWebhookEvent(WebhookRequest request){
        paymentGateway
            .parseWebhookRequest(request)
            .ifPresent(paymentResult -> {
                var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                order.setStatus(paymentResult.getPaymentStatus());
                orderRepository.save(order);
            });
    }
}
