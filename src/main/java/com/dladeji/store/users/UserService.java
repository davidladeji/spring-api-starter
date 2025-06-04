package com.dladeji.store.users;

import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto getUser(Long userId){
        var user = getUserObj(userId);
        return userMapper.toDto(user);
    }

    public Iterable<UserDto> getAllUsers(String sortBy){
        if (!Set.of("name", "email").contains(sortBy)) 
            sortBy = "name";
        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto registerUser(RegisterUserRequest request){
        if (userRepository.existsByEmail(request.getEmail()))
            throw new UserAlreadyExistsException();

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long userId, UpdateUserRequest request){
        var user = getUserObj(userId);
        userMapper.update(request, user);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public void deleteUser(Long userId){
        var user = getUserObj(userId);
        userRepository.delete(user);
    }

    public void updatePassword(Long userId, ChangePasswordRequest request){
        var user = getUserObj(userId);
        
        if (!user.getPassword().equals(request.getOldPassword()))
            throw new WrongPasswordException();

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

    private User getUserObj(Long userId){
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
