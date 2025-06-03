package com.dladeji.store.services;

import java.util.Optional;

import com.dladeji.store.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
