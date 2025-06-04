package com.dladeji.store.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dladeji.store.users.User;
import com.dladeji.store.users.UserDto;
import com.dladeji.store.users.UserMapper;
import com.dladeji.store.users.UserNotFoundException;
import com.dladeji.store.users.UserRepository;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    
    public User getCurrentUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();
        var user = userRepository.findById(userId).orElseThrow();
        return user;
    }

    public UserDto getAuthUser(){
        var user = getCurrentUser();
        if (user == null)
            throw new UserNotFoundException();
        
        return userMapper.toDto(user);
    }

    public JwtResponse refresh(String refreshToken){
        var jwt = jwtService.parse(refreshToken);
        if (jwt == null || jwt.isExpired())
            throw new BadCredentialsException("Invalid Refresh token");
        
        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);

        return new JwtResponse(accessToken.toString());
    }

    public LoginResponse login(LoginUserDto request ){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = generateRefreshTokenCookie(refreshToken.toString());
        return new LoginResponse(cookie, new JwtResponse(accessToken.toString()));
    }

    private Cookie generateRefreshTokenCookie(String refreshToken){
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); // 7 days
        cookie.setSecure(true);
        return cookie;
    }
}
