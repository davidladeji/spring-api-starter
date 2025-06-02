package com.dladeji.store.services;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dladeji.store.dtos.OrderCheckoutDto;
import com.dladeji.store.entities.Order;
import com.dladeji.store.exceptions.CartIsEmptyException;
import com.dladeji.store.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartService cartService;
    private final AuthService authService;
    private final OrderRepository orderRepository;

    @Value("${websiteUrl}")
    private String websiteUrl;
    
    @Transactional
    public OrderCheckoutDto checkout(UUID cartId) throws StripeException{
        var cart = cartService.getCartObj(cartId);

        if (cart.getItems().isEmpty())
            throw new CartIsEmptyException();
        var user = authService.getCurrentUser();

        
        var order = Order.fromCart(cart, user);
        orderRepository.save(order);

        try {
            // Create a checkout session
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel");

            order.getItems().forEach(item -> {
                var lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(Long.valueOf(item.getQuantity()))
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmountDecimal(item.getUnitPrice()
                                .multiply(BigDecimal.valueOf(100)))
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(item.getProduct().getName())
                                    .build()
                            )
                            .build()
                    )
                    .build();
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());


            cartService.clearCart(cartId);

            return new OrderCheckoutDto(order.getId(), session.getUrl());
        } catch (StripeException ex){
            System.out.println(ex.getMessage());
            orderRepository.delete(order);
            throw ex;
        }
    }
}
