package com.dladeji.store.controllers;

import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.dladeji.store.dtos.RegisterUserRequest;
import com.dladeji.store.dtos.UpdateUserRequest;
import com.dladeji.store.dtos.UserDto;
import com.dladeji.store.entities.User;
import com.dladeji.store.mappers.UserMapper;
import com.dladeji.store.repositories.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;
    private UserMapper userMapper;
    
    @GetMapping
    public Iterable<UserDto> getAllUsers(
        @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
    ) {
        if (    !Set.of("name", "email").contains(sortBy)) 
            sortBy = "name";
        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }
    
    @PostMapping
    public ResponseEntity<UserDto> createUser(
        @RequestBody RegisterUserRequest request,
        UriComponentsBuilder uriBuilder
        ){
        var user = userMapper.toEntity(request);
        userRepository.save(user);

        var userDto = userMapper.toDto(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
        @PathVariable Long id, 
        @RequestBody UpdateUserRequest request
        ) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ResponseEntity.notFound().build();
        
        userMapper.update(request, user);
        userRepository.save(user);

        var userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);

        if (user == null)
            return ResponseEntity.notFound().build();

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }
}
