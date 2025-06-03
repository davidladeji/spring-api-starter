package com.dladeji.store.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dladeji.store.entities.Order;
import com.dladeji.store.entities.OrderItem;
import com.dladeji.store.exceptions.PaymentException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;

@Service
public class StripePaymentGateway implements PaymentGateway{
    @Value("${websiteUrl}")
    private String websiteUrl;

    public CheckoutSession createCheckoutSession(Order order){
        // Create a checkout session
        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel");

            order.getItems().forEach(item -> {
                var lineItem = createLineItem(item);
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        } catch (StripeException ex){
            System.out.println(ex.getMessage());
            throw new PaymentException();
        }
    }

    private LineItem createLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
            .setQuantity(Long.valueOf(item.getQuantity()))
            .setPriceData(
                createPriceData(item)
            )
            .build();
    }

    private PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
            .setCurrency("usd")
            .setUnitAmountDecimal(item.getUnitPrice()
                .multiply(BigDecimal.valueOf(100)))
            .setProductData(
                createProductData(item)
            )
            .build();
    }

    private ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
            .setName(item.getProduct().getName())
            .build();
    }
}
