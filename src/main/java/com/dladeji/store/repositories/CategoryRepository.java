package com.dladeji.store.repositories;

import org.springframework.data.repository.CrudRepository;

import com.dladeji.store.entities.Category;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}