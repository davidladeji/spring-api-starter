package com.dladeji.store.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dladeji.store.users.User;
import com.dladeji.store.users.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
    private UserRepository userRepository;
    
    public User getCurrentUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();
        var user = userRepository.findById(userId).orElseThrow();
        return user;
    }
}
