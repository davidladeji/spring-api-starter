package com.dladeji.store.carts;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemDto {
    private ProductDto product;
    private int quantity;
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
