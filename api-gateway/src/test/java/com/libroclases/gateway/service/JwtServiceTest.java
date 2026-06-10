package com.libroclases.gateway.service;

import com.libroclases.gateway.model.Role;
import com.libroclases.gateway.model.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret",
                "clave-secreta-de-pruebas-suficientemente-larga-para-hs256");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3600000L);
    }

    private User docente() {
        return User.builder()
                .id(1L)
                .email("ana@colegio.cl")
                .nombre("Ana Díaz")
                .entityId(5L)
                .roles(Set.of(Role.builder().id(1L).nombre("DOCENTE").build()))
                .build();
    }

    @Test
    void generaUnTokenYLoParseaConElEmailComoSubject() {
        String token = jwtService.generate(docente());

        Claims claims = jwtService.parse(token);

        assertThat(claims.getSubject()).isEqualTo("ana@colegio.cl");
        assertThat(claims.get("nombre")).isEqualTo("Ana Díaz");
    }

    @Test
    void rolesFromDevuelveLosRolesDelToken() {
        Claims claims = jwtService.parse(jwtService.generate(docente()));

        assertThat(jwtService.rolesFrom(claims)).containsExactly("DOCENTE");
    }

    @Test
    void rolesFromDevuelveVacioCuandoElUsuarioNoTieneRoles() {
        User sinRoles = User.builder()
                .id(2L)
                .email("sin@colegio.cl")
                .nombre("Sin Roles")
                .roles(Set.of())
                .build();

        Claims claims = jwtService.parse(jwtService.generate(sinRoles));

        assertThat(jwtService.rolesFrom(claims)).isEmpty();
    }
}
