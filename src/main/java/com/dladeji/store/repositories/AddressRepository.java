package com.dladeji.store.repositories;

import org.springframework.data.repository.CrudRepository;

import com.dladeji.store.entities.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
}