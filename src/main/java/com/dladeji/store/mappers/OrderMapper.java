package com.dladeji.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dladeji.store.dtos.OrderDto;
import com.dladeji.store.entities.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "orderId", source = "order.id")
    OrderDto toDto(Order order);
}
