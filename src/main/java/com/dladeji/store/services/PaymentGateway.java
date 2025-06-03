package com.dladeji.store.services;

import com.dladeji.store.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
}
