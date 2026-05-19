package com.libroclases.gateway.service;

import com.libroclases.gateway.dto.LoginRequest;
import com.libroclases.gateway.dto.LoginResponse;
import com.libroclases.gateway.dto.RegisterRequest;
import com.libroclases.gateway.exception.InvalidCredentialsException;
import com.libroclases.gateway.model.Role;
import com.libroclases.gateway.model.User;
import com.libroclases.gateway.repository.RoleRepository;
import com.libroclases.gateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Transactional
    public LoginResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        Role role = roleRepository.findByNombre(req.getRole())
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + req.getRole()));

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = User.builder()
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .nombre(req.getNombre())
                .entityId(req.getEntityId())
                .roles(roles)
                .build();
        user = userRepository.save(user);

        return LoginResponse.builder()
                .token(jwtService.generate(user))
                .user(userMapper.toDTO(user))
                .build();
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return LoginResponse.builder()
                .token(jwtService.generate(user))
                .user(userMapper.toDTO(user))
                .build();
    }
}
