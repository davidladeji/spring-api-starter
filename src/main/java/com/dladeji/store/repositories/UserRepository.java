package com.dladeji.store.repositories;

import org.springframework.data.repository.CrudRepository;

import com.dladeji.store.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
