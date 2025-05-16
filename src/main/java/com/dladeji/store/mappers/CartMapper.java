package com.dladeji.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dladeji.store.dtos.CartDto;
import com.dladeji.store.dtos.CartItemDto;
import com.dladeji.store.entities.Cart;
import com.dladeji.store.entities.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
