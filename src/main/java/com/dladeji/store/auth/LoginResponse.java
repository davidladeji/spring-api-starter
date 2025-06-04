package com.dladeji.store.auth;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Cookie cookie;
    private JwtResponse jwtResponse;
}
