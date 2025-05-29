package com.dladeji.store.services;

import java.util.Date;

import javax.crypto.SecretKey;

import com.dladeji.store.entities.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class Jwt {
    private Claims claims;
    private SecretKey key;

    public Jwt(Claims claims, SecretKey key){
        this.claims = claims;
        this.key = key;
    }

    public boolean isExpired(){
        return claims.getExpiration().before(new Date());
    }

    public Long getUserId(){
        return Long.valueOf(claims.getSubject());
    }

    public Role getRole(){
        return Role.valueOf(claims.get("role", String.class));
    }

    public String toString(){
        return Jwts.builder().claims(claims).signWith(key).compact();
    }
}
