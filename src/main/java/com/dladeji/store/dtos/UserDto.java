package com.dladeji.store.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {
    // @JsonIgnore to ignore the field when serializing to JSON
    // @JsonProperty to specify the name of the field in JSON
    // @JsonInclude to include the field only if it's not null
    @JsonProperty("user_id")
    private Long id;
    private String name;
    private String email;
}
