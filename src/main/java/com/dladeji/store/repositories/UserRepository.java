package com.dladeji.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dladeji.store.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
