package com.dladeji.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.dladeji.store.dtos.RegisterUserRequest;
import com.dladeji.store.dtos.UpdateUserRequest;
import com.dladeji.store.dtos.UserDto;
import com.dladeji.store.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest request);

    void update(UpdateUserRequest request, @MappingTarget User user);
}
