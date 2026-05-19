package com.libroclases.gateway.config;

import com.libroclases.gateway.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        HttpServletRequest forwarded = request;

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwtService.parse(token);
                String email = claims.getSubject();
                Set<String> roles = jwtService.rolesFrom(claims);
                var authorities = roles.stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toSet());
                var auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);

                Map<String, String> extra = new LinkedHashMap<>();
                Object userId = claims.get("userId");
                if (userId != null) extra.put("X-User-Id", userId.toString());
                Object nombre = claims.get("nombre");
                if (nombre != null) extra.put("X-User-Name", nombre.toString());
                if (email != null) extra.put("X-User-Email", email);
                Object entityId = claims.get("entityId");
                if (entityId != null) extra.put("X-User-Entity-Id", entityId.toString());
                if (!roles.isEmpty()) extra.put("X-User-Role", roles.iterator().next());

                forwarded = new HeaderInjectingRequest(request, extra);
            } catch (JwtException e) {
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(forwarded, response);
    }

    private static final class HeaderInjectingRequest extends HttpServletRequestWrapper {
        private final Map<String, String> extras;

        HeaderInjectingRequest(HttpServletRequest request, Map<String, String> extras) {
            super(request);
            this.extras = extras;
        }

        @Override
        public String getHeader(String name) {
            for (Map.Entry<String, String> e : extras.entrySet()) {
                if (e.getKey().equalsIgnoreCase(name)) return e.getValue();
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            for (Map.Entry<String, String> e : extras.entrySet()) {
                if (e.getKey().equalsIgnoreCase(name)) {
                    return Collections.enumeration(List.of(e.getValue()));
                }
            }
            return super.getHeaders(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Set<String> names = new HashSet<>();
            Enumeration<String> original = super.getHeaderNames();
            while (original.hasMoreElements()) names.add(original.nextElement());
            names.addAll(extras.keySet());
            Iterator<String> it = names.iterator();
            return new Enumeration<>() {
                @Override public boolean hasMoreElements() { return it.hasNext(); }
                @Override public String nextElement() { return it.next(); }
            };
        }
    }
}
