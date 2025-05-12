package com.dladeji.store.repositories;

import org.springframework.data.repository.CrudRepository;

import com.dladeji.store.entities.Profile;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}