package com.dladeji.store.services;
import com.dladeji.store.repositories.CartItemRepository;
import com.dladeji.store.repositories.CartRepository;
import com.dladeji.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dladeji.store.dtos.CartDto;
import com.dladeji.store.dtos.CartItemDto;
import com.dladeji.store.entities.Cart;
import com.dladeji.store.exceptions.CartNotFoundException;
import com.dladeji.store.exceptions.ProductNotFoundException;
import com.dladeji.store.mappers.CartMapper;

@Service
@AllArgsConstructor
public class CartService {
    private CartMapper cartMapper;
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private CartItemRepository cartItemRepository;

    public CartDto createCart(){
        Cart cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        var product = productRepository.findById(productId).orElse(null);
        if (product == null)
            throw new ProductNotFoundException();
        
        var cartItem = cart.addItem(product);
        cartItemRepository.save(cartItem);

        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();
            
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCartItem(
        UUID cartId, 
        Long productId,
        int quantity
    ){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        var cartItem = cart.getItem(productId);
        if (cartItem == null) 
            throw new ProductNotFoundException();

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        return cartMapper.toDto(cartItem);
    }

    public void removeProductFromCart(UUID cartId, Long productId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        cart.clear();
        cartRepository.save(cart);
    }
}
