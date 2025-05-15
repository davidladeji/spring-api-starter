package com.dladeji.store.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.dladeji.store.dtos.CartDto;
import com.dladeji.store.entities.Cart;
import com.dladeji.store.mappers.CartMapper;
import com.dladeji.store.repositories.CartRepository;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private CartRepository cartRepository;
    private CartMapper cartMapper;

    @PostMapping
    public ResponseEntity<?> createCart(
        UriComponentsBuilder uriBuilder
    ) {
        // var cart
        Cart cart = new Cart();
        cartRepository.save(cart);

        System.out.println(cart);

        CartDto cartDto = cartMapper.toDto(cart);
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cart.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }
}
