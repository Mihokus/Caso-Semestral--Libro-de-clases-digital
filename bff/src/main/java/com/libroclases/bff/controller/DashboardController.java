package com.libroclases.bff.controller;

import com.libroclases.bff.dto.dashboard.DashboardDTO;
import com.libroclases.bff.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;

    @GetMapping("/api/dashboard")
    public DashboardDTO get(
            @RequestHeader(value = "X-User-Id", defaultValue = "0") Long userId,
            @RequestHeader(value = "X-User-Name", defaultValue = "anonymous") String userName,
            @RequestHeader(value = "X-User-Email", defaultValue = "") String email,
            @RequestHeader(value = "X-User-Role", defaultValue = "ESTUDIANTE") String role,
            @RequestHeader(value = "X-User-Entity-Id", required = false) Long entityId) {
        return service.build(userId, userName, email, role, entityId);
    }
}
