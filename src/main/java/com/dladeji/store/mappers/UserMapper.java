package com.dladeji.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dladeji.store.dtos.UserDto;
import com.dladeji.store.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
