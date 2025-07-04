package com.dladeji.store.carts;
import lombok.AllArgsConstructor;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dladeji.store.products.ProductNotFoundException;
import com.dladeji.store.products.ProductRepository;

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

    public Cart getCartObj(UUID cartId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();
            
        return cart;
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
