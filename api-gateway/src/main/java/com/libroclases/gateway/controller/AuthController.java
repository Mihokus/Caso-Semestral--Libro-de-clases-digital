package com.libroclases.gateway.controller;

import com.libroclases.gateway.dto.InviteStudentRequest;
import com.libroclases.gateway.dto.LoginRequest;
import com.libroclases.gateway.dto.LoginResponse;
import com.libroclases.gateway.dto.RegisterRequest;
import com.libroclases.gateway.dto.SetPasswordRequest;
import com.libroclases.gateway.dto.UserDTO;
import com.libroclases.gateway.model.User;
import com.libroclases.gateway.repository.UserRepository;
import com.libroclases.gateway.service.AuthService;
import com.libroclases.gateway.service.JwtService;
import com.libroclases.gateway.service.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/login/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(req));
    }

    @PostMapping("/invite-student")
    public ResponseEntity<UserDTO> inviteStudent(@Valid @RequestBody InviteStudentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.inviteStudent(req));
    }

    @PostMapping("/login/set-password")
    public ResponseEntity<LoginResponse> setPassword(@Valid @RequestBody SetPasswordRequest req) {
        return ResponseEntity.ok(authService.setPassword(req));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Missing bearer token"));
        }
        try {
            Claims claims = jwtService.parse(authHeader.substring(7));
            Long userId = claims.get("userId", Long.class);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new JwtException("User not found"));
            UserDTO dto = userMapper.toDTO(user);
            return ResponseEntity.ok(dto);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token: " + e.getMessage()));
        }
    }
}
