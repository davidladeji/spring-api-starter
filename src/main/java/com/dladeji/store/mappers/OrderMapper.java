package com.dladeji.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dladeji.store.dtos.OrderCheckoutDto;
import com.dladeji.store.dtos.OrderDto;
import com.dladeji.store.dtos.OrderItemDto;
import com.dladeji.store.dtos.ProductOrderDto;
import com.dladeji.store.entities.Order;
import com.dladeji.store.entities.OrderItem;
import com.dladeji.store.entities.Product;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    // @Mapping(target = "orderId", source = "order.id")
    // OrderCheckoutDto toCheckoutDto(Order order);

    OrderDto toDto(Order order);
    Order toEntity(OrderDto orderDto);

    OrderItemDto toDto(OrderItem orderItem);
    OrderItem toEntity(OrderItem orderItemDto);

    ProductOrderDto toDto(Product product);
}
