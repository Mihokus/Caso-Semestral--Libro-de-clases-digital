package com.libroclases.gateway.controller;

import com.libroclases.gateway.dto.MenuDTO;
import com.libroclases.gateway.dto.MenuRequest;
import com.libroclases.gateway.dto.RoleDTO;
import com.libroclases.gateway.dto.UpdateRolesRequest;
import com.libroclases.gateway.dto.UserDTO;
import com.libroclases.gateway.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ---- Users ----

    @GetMapping("/users")
    public List<UserDTO> listUsers() {
        return adminService.listUsers();
    }

    @PutMapping("/users/{id}/roles")
    public UserDTO updateUserRoles(@PathVariable Long id, @Valid @RequestBody UpdateRolesRequest req) {
        return adminService.updateUserRoles(id, req);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Roles ----

    @GetMapping("/roles")
    public List<RoleDTO> listRoles() {
        return adminService.listRoles();
    }

    @PostMapping("/roles")
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createRole(req));
    }

    // ---- Menus ----

    @GetMapping("/menus")
    public List<MenuDTO> listMenus() {
        return adminService.listMenus();
    }

    @PostMapping("/menus")
    public ResponseEntity<MenuDTO> createMenu(@Valid @RequestBody MenuRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createMenu(req));
    }

    @PutMapping("/menus/{id}/roles")
    public MenuDTO updateMenuRoles(@PathVariable Long id, @Valid @RequestBody UpdateRolesRequest req) {
        return adminService.updateMenuRoles(id, req);
    }
}
