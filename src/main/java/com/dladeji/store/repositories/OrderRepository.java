package com.dladeji.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dladeji.store.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
