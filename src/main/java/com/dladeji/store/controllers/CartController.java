package com.dladeji.store.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.dladeji.store.dtos.AddCartItemRequest;
import com.dladeji.store.dtos.CartDto;
import com.dladeji.store.dtos.UpdateCartItemDto;
import com.dladeji.store.entities.Cart;
import com.dladeji.store.mappers.CartMapper;
import com.dladeji.store.repositories.CartItemRepository;
import com.dladeji.store.repositories.CartRepository;
import com.dladeji.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;




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
        
        var cartItem = cart.addItem(product);
        cartItemRepository.save(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartMapper.toDto(cartItem));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<?> getCart(
        @PathVariable UUID cartId
    ) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null)
            return ResponseEntity.notFound().build();
            
        var cartDto = cartMapper.toDto(cart);
        return ResponseEntity.ok().body(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(
        @PathVariable UUID cartId,
        @PathVariable Long productId, 
        @RequestBody UpdateCartItemDto request
    ) {
        // Validate id args
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart not found.")
            );
        }

        var quantity = request.getQuantity();
        if (quantity < 1 || quantity > 100){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "Quantity needs to be between 1 and 100")
            );
        }

        var cartItem = cart.getItem(productId);

        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Product was not found in the cart.")
            );
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        return ResponseEntity.ok(cartMapper.toDto(cartItem));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeProductFromCart(
        @PathVariable UUID cartId,
        @PathVariable Long productId
    ){
        // Validate id args
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart not found.")
            );
        } 

        cart.removeItem(productId);
        cartRepository.save(cart);
        return ResponseEntity.noContent().build();
    }
    
    
}
