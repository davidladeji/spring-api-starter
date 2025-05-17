package com.dladeji.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dladeji.store.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
