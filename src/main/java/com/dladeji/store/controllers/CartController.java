package com.dladeji.store.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.dladeji.store.dtos.AddCartItemRequest;
import com.dladeji.store.dtos.CartDto;
import com.dladeji.store.entities.Cart;
import com.dladeji.store.entities.CartItem;
import com.dladeji.store.mappers.CartMapper;
import com.dladeji.store.repositories.CartItemRepository;
import com.dladeji.store.repositories.CartRepository;
import com.dladeji.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;
    private CartItemRepository cartItemRepository;

    @PostMapping
    public ResponseEntity<?> createCart(
        UriComponentsBuilder uriBuilder
    ) {
        Cart cart = new Cart();
        cartRepository.save(cart);

        CartDto cartDto = cartMapper.toDto(cart);
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cart.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addProductToCart(
        @PathVariable UUID cartId,
        @RequestBody AddCartItemRequest request
    ) {
        Long productId = request.getProductId();
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null)
            return ResponseEntity.notFound().build();

        var product = productRepository.findById(productId).orElse(null);
        if (product == null)
            return ResponseEntity.badRequest().build();
        

        if (cartItemRepository.existsByProductId(productId)){
            var cartItem = cartItemRepository.findByProductId(productId).orElse(null);
            cartItem.setQuantity(cartItem.getQuantity()+1);
            cartItemRepository.save(cartItem);
            return ResponseEntity.ok().body(cartMapper.toDto(cartItem));
        }

        var cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItem.setQuantity(1);
        cartItemRepository.save(cartItem);

        return ResponseEntity.ok().body(cartMapper.toDto(cartItem));
    }
    
}
