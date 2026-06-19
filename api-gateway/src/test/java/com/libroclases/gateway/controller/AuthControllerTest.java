package com.libroclases.gateway.controller;

import com.libroclases.gateway.dto.LoginRequest;
import com.libroclases.gateway.dto.LoginResponse;
import com.libroclases.gateway.dto.RegisterRequest;
import com.libroclases.gateway.dto.UserDTO;
import com.libroclases.gateway.model.User;
import com.libroclases.gateway.repository.UserRepository;
import com.libroclases.gateway.service.AuthService;
import com.libroclases.gateway.service.JwtService;
import com.libroclases.gateway.service.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock private AuthService authService;
    @Mock private JwtService jwtService;
    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private Claims claims;

    @InjectMocks private AuthController controller;

    @Test
    void loginDevuelve200ConElToken() {
        LoginResponse resp = LoginResponse.builder().token("token-jwt").build();
        when(authService.login(any(LoginRequest.class))).thenReturn(resp);

        ResponseEntity<LoginResponse> res = controller.login(new LoginRequest());

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isEqualTo(resp);
    }

    @Test
    void registerDevuelve201() {
        LoginResponse resp = LoginResponse.builder().token("token-jwt").build();
        when(authService.register(any(RegisterRequest.class))).thenReturn(resp);

        ResponseEntity<LoginResponse> res = controller.register(new RegisterRequest());

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).isEqualTo(resp);
    }

    @Test
    void meSinHeaderDevuelve401() {
        ResponseEntity<?> res = controller.me(null);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void meConTokenValidoDevuelveElUsuario() {
        UserDTO dto = UserDTO.builder().id(1L).email("ana@colegio.cl").build();
        User user = User.builder().id(1L).email("ana@colegio.cl").build();
        when(jwtService.parse("token-xyz")).thenReturn(claims);
        when(claims.get("userId", Long.class)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        ResponseEntity<?> res = controller.me("Bearer token-xyz");

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isEqualTo(dto);
    }

    @Test
    void meConTokenInvalidoDevuelve401() {
        when(jwtService.parse(eq("malo"))).thenThrow(new JwtException("inválido"));

        ResponseEntity<?> res = controller.me("Bearer malo");

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
