package com.dladeji.store.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dladeji.store.dtos.CheckoutRequest;
import com.dladeji.store.dtos.ErrorDto;
import com.dladeji.store.dtos.OrderCheckoutDto;
import com.dladeji.store.entities.OrderStatus;
import com.dladeji.store.exceptions.PaymentException;
import com.dladeji.store.repositories.OrderRepository;
import com.dladeji.store.services.CheckoutService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;
    
    @PostMapping
    public OrderCheckoutDto checkout(
        @Valid @RequestBody CheckoutRequest request
    ){
        
        return checkoutService.checkout(request.getCartId());
        
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
        @RequestHeader("Stripe-Signature") String signature,
        @RequestBody String payload
    ){
        try {
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);
            System.out.println(event.getType());

            var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
            
            switch (event.getType()){
                case "payment_intent.succeeded" -> {
                    var paymentIntent = (PaymentIntent)(stripeObject);
                    if (paymentIntent != null) {
                        var orderId = paymentIntent.getMetadata().get("order_id");
                        var order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow();
                        order.setStatus(OrderStatus.PAID);
                        orderRepository.save(order);
                    }
                }
                case "payment_intent.failed" -> {
                    // Update order status (FAILED)
                }
            }

            return ResponseEntity.ok().build();

        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorDto> handlePaymentException(){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating a checkout service"));
    }
}
