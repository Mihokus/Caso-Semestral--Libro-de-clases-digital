package com.libroclases.gateway.config;

import com.libroclases.gateway.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock private JwtService jwtService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain chain;
    @Mock private Claims claims;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void conTokenValidoAutenticaEInyectaLosHeaders() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        when(request.getHeader("Authorization")).thenReturn("Bearer token-xyz");
        when(jwtService.parse("token-xyz")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("ana@colegio.cl");
        when(jwtService.rolesFrom(claims)).thenReturn(Set.of("DOCENTE"));
        when(claims.get("userId")).thenReturn(1L);
        when(claims.get("nombre")).thenReturn("Ana");
        when(claims.get("entityId")).thenReturn(5L);
        when(request.getHeaderNames()).thenReturn(Collections.emptyEnumeration());

        filter.doFilterInternal(request, response, chain);

        ArgumentCaptor<HttpServletRequest> captor = ArgumentCaptor.forClass(HttpServletRequest.class);
        verify(chain).doFilter(captor.capture(), any());
        HttpServletRequest forwarded = captor.getValue();
        assertThat(forwarded.getHeader("X-User-Id")).isEqualTo("1");
        assertThat(forwarded.getHeader("X-User-Name")).isEqualTo("Ana");
        assertThat(forwarded.getHeader("X-User-Email")).isEqualTo("ana@colegio.cl");
        assertThat(forwarded.getHeader("X-User-Role")).isEqualTo("DOCENTE");
        assertThat(Collections.list(forwarded.getHeaders("X-User-Id"))).containsExactly("1");
        assertThat(Collections.list(forwarded.getHeaderNames())).contains("X-User-Id", "X-User-Email");
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    }

    @Test
    void sinHeaderAuthorizationPasaDeLargo() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void conTokenInvalidoLimpiaElContextoYPasaDeLargo() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        when(request.getHeader("Authorization")).thenReturn("Bearer malo");
        when(jwtService.parse("malo")).thenThrow(new JwtException("token inválido"));

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
