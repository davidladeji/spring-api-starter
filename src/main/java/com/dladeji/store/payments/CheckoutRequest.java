package com.dladeji.store.payments;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutRequest {
    @NotNull(message = "Valid Id is required")
    private UUID cartId; 
}
