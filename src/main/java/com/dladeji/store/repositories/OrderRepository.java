package com.dladeji.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dladeji.store.entities.Order;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
