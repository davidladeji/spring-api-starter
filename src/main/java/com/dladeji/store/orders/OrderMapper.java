package com.dladeji.store.orders;

import org.mapstruct.Mapper;

import com.dladeji.store.products.Product;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toDto(Order order);
    Order toEntity(OrderDto orderDto);

    OrderItemDto toDto(OrderItem orderItem);

    ProductDto toDto(Product product);
}
