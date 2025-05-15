package com.dladeji.store.mappers;

import org.mapstruct.Mapper;

import com.dladeji.store.dtos.CartDto;
import com.dladeji.store.entities.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
}
