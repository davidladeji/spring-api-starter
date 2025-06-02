package com.dladeji.store.mappers;

import org.mapstruct.Mapper;

import com.dladeji.store.dtos.OrderDto;
import com.dladeji.store.dtos.OrderItemDto;
import com.dladeji.store.dtos.ProductOrderDto;
import com.dladeji.store.entities.Order;
import com.dladeji.store.entities.OrderItem;
import com.dladeji.store.entities.Product;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toDto(Order order);
    Order toEntity(OrderDto orderDto);

    OrderItemDto toDto(OrderItem orderItem);

    ProductOrderDto toDto(Product product);
}
