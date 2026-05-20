package com.libroclases.gateway.service;

import com.libroclases.gateway.dto.InviteStudentRequest;
import com.libroclases.gateway.dto.LoginRequest;
import com.libroclases.gateway.dto.LoginResponse;
import com.libroclases.gateway.dto.RegisterRequest;
import com.libroclases.gateway.dto.SetPasswordRequest;
import com.libroclases.gateway.dto.UserDTO;
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
        if (!"ESTUDIANTE".equals(req.getRole()) && !"APODERADO".equals(req.getRole())) {
            throw new IllegalArgumentException("El registro público solo permite los roles ESTUDIANTE o APODERADO");
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
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            return LoginResponse.builder().mustSetPassword(true).build();
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return LoginResponse.builder()
                .token(jwtService.generate(user))
                .user(userMapper.toDTO(user))
                .build();
    }

    @Transactional
    public UserDTO inviteStudent(InviteStudentRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email ya registrado");
        }
        Role role = roleRepository.findByNombre("ESTUDIANTE")
                .orElseThrow(() -> new IllegalArgumentException("Rol ESTUDIANTE no encontrado"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = User.builder()
                .email(req.getEmail())
                .passwordHash(null)
                .nombre(req.getNombre())
                .entityId(req.getAlumnoId())
                .roles(roles)
                .build();
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Transactional
    public LoginResponse setPassword(SetPasswordRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
        if (user.getPasswordHash() != null && !user.getPasswordHash().isBlank()) {
            throw new IllegalArgumentException("El usuario ya tiene contraseña definida");
        }
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user = userRepository.save(user);
        return LoginResponse.builder()
                .token(jwtService.generate(user))
                .user(userMapper.toDTO(user))
                .build();
    }
}
