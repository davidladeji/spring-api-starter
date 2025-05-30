package com.dladeji.store.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductOrderDto {
    private Long id;
    private String name;
    private BigDecimal price;
}
