package com.dladeji.store.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemDto {
    private ProductOrderDto product;
    private int quantity;
    private BigDecimal totalPrice;
}
