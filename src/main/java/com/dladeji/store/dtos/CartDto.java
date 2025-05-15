package com.dladeji.store.dtos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.dladeji.store.entities.CartItem;

import lombok.Data;

@Data
public class CartDto {
    private UUID id;
    private List<CartItem> items = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
