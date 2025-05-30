package com.dladeji.store.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.dladeji.store.entities.OrderStatus;

import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private Set<OrderItemDto> items = new LinkedHashSet();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
