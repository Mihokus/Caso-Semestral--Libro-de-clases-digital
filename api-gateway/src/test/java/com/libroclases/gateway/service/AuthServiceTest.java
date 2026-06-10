package com.libroclases.gateway.service;

import com.libroclases.gateway.dto.LoginRequest;
import com.libroclases.gateway.dto.LoginResponse;
import com.libroclases.gateway.dto.RegisterRequest;
import com.libroclases.gateway.dto.UserDTO;
import com.libroclases.gateway.exception.InvalidCredentialsException;
import com.libroclases.gateway.model.Role;
import com.libroclases.gateway.model.User;
import com.libroclases.gateway.repository.RoleRepository;
import com.libroclases.gateway.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private UserMapper userMapper;

    @InjectMocks private AuthService authService;

    private RegisterRequest registerReq() {
        RegisterRequest r = new RegisterRequest();
        r.setEmail("nuevo@colegio.cl");
        r.setPassword("clave123");
        r.setNombre("Nuevo Usuario");
        r.setRole("DOCENTE");
        r.setEntityId(null);
        return r;
    }

    private LoginRequest loginReq() {
        LoginRequest r = new LoginRequest();
        r.setEmail("ana@colegio.cl");
        r.setPassword("secreta");
        return r;
    }

    @Test
    void registerCreaUsuarioYDevuelveToken() {
        when(userRepository.existsByEmail("nuevo@colegio.cl")).thenReturn(false);
        when(roleRepository.findByNombre("DOCENTE"))
                .thenReturn(Optional.of(Role.builder().nombre("DOCENTE").build()));
        when(passwordEncoder.encode("clave123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtService.generate(any(User.class))).thenReturn("token-jwt");
        when(userMapper.toDTO(any(User.class)))
                .thenReturn(UserDTO.builder().email("nuevo@colegio.cl").build());

        LoginResponse res = authService.register(registerReq());

        assertThat(res.getToken()).isEqualTo("token-jwt");
        assertThat(res.getUser().getEmail()).isEqualTo("nuevo@colegio.cl");
    }

    @Test
    void registerFallaSiElEmailYaEstaRegistrado() {
        when(userRepository.existsByEmail("nuevo@colegio.cl")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerReq()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void registerFallaSiElRolNoExiste() {
        when(userRepository.existsByEmail("nuevo@colegio.cl")).thenReturn(false);
        when(roleRepository.findByNombre("DOCENTE")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.register(registerReq()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void loginExitosoDevuelveToken() {
        User user = User.builder()
                .id(1L).email("ana@colegio.cl").passwordHash("hash").nombre("Ana").build();
        when(userRepository.findByEmail("ana@colegio.cl")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secreta", "hash")).thenReturn(true);
        when(jwtService.generate(user)).thenReturn("token-jwt");
        when(userMapper.toDTO(user)).thenReturn(UserDTO.builder().email("ana@colegio.cl").build());

        LoginResponse res = authService.login(loginReq());

        assertThat(res.getToken()).isEqualTo("token-jwt");
    }

    @Test
    void loginFallaSiElUsuarioNoExiste() {
        when(userRepository.findByEmail("ana@colegio.cl")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginReq()))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void loginFallaSiLaPasswordEsIncorrecta() {
        User user = User.builder().email("ana@colegio.cl").passwordHash("hash").build();
        when(userRepository.findByEmail("ana@colegio.cl")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secreta", "hash")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(loginReq()))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
