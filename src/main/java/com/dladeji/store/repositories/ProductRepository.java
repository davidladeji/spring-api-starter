package com.dladeji.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dladeji.store.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}