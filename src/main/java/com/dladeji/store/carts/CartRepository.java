package com.dladeji.store.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dladeji.store.entities.Cart;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}
