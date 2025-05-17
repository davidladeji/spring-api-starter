package com.dladeji.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dladeji.store.dtos.CartDto;
import com.dladeji.store.dtos.CartItemDto;
import com.dladeji.store.dtos.UpdateCartItemDto;
import com.dladeji.store.entities.Cart;
import com.dladeji.store.entities.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    void updateCartItem(UpdateCartItemDto request, @MappingTarget CartItem cartItem);
}
