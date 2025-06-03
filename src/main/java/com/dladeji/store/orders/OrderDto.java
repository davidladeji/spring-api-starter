package com.dladeji.store.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private Set<OrderItemDto> items = new LinkedHashSet<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
