package com.libroclases.gateway.controller;

import com.libroclases.gateway.exception.InvalidCredentialsException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapeaIllegalArgumentExceptionA400() {
        ResponseEntity<?> res = handler.handleBadRequest(new IllegalArgumentException("dato inválido"));

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat((Map<String, Object>) res.getBody()).containsEntry("error", "dato inválido");
    }

    @Test
    void mapeaInvalidCredentialsExceptionA401() {
        ResponseEntity<?> res = handler.handleUnauthorized(new InvalidCredentialsException("credenciales inválidas"));

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat((Map<String, Object>) res.getBody()).containsEntry("error", "credenciales inválidas");
    }
}
