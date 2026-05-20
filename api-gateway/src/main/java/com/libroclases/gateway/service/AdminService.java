package com.libroclases.gateway.service;

import com.libroclases.gateway.dto.MenuDTO;
import com.libroclases.gateway.dto.MenuRequest;
import com.libroclases.gateway.dto.RoleDTO;
import com.libroclases.gateway.dto.UpdateRolesRequest;
import com.libroclases.gateway.dto.UserDTO;
import com.libroclases.gateway.model.Menu;
import com.libroclases.gateway.model.Role;
import com.libroclases.gateway.model.User;
import com.libroclases.gateway.repository.MenuRepository;
import com.libroclases.gateway.repository.RoleRepository;
import com.libroclases.gateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;
    private final UserMapper userMapper;

    // ---- Users ----

    @Transactional(readOnly = true)
    public List<UserDTO> listUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Transactional
    public UserDTO updateUserRoles(Long userId, UpdateRolesRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(req.getRoleIds()));
        if (roles.size() != req.getRoleIds().size()) {
            throw new IllegalArgumentException("Some role IDs not found");
        }
        user.setRoles(roles);
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        userRepository.deleteById(userId);
    }

    // ---- Roles ----

    @Transactional(readOnly = true)
    public List<RoleDTO> listRoles() {
        return roleRepository.findAll().stream()
                .map(this::toRoleDTO)
                .toList();
    }

    @Transactional
    public RoleDTO createRole(RoleDTO req) {
        if (roleRepository.existsByNombre(req.getNombre())) {
            throw new IllegalArgumentException("Role already exists: " + req.getNombre());
        }
        Role role = Role.builder()
                .nombre(req.getNombre())
                .descripcion(req.getDescripcion())
                .build();
        return toRoleDTO(roleRepository.save(role));
    }

    private RoleDTO toRoleDTO(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .nombre(role.getNombre())
                .descripcion(role.getDescripcion())
                .build();
    }

    // ---- Menus ----

    @Transactional(readOnly = true)
    public List<MenuDTO> listMenus() {
        return menuRepository.findAll().stream()
                .map(this::toMenuDTO)
                .toList();
    }

    @Transactional
    public MenuDTO createMenu(MenuRequest req) {
        Set<Role> roles = req.getRoleIds() == null
                ? new HashSet<>()
                : new HashSet<>(roleRepository.findAllById(req.getRoleIds()));
        Menu menu = Menu.builder()
                .label(req.getLabel())
                .path(req.getPath())
                .parentId(req.getParentId())
                .orden(req.getOrden())
                .roles(roles)
                .build();
        return toMenuDTO(menuRepository.save(menu));
    }

    @Transactional
    public MenuDTO updateMenuRoles(Long menuId, UpdateRolesRequest req) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found: " + menuId));
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(req.getRoleIds()));
        menu.setRoles(roles);
        menuRepository.save(menu);
        return toMenuDTO(menu);
    }

    private MenuDTO toMenuDTO(Menu menu) {
        Set<String> roleNames = menu.getRoles().stream()
                .map(Role::getNombre)
                .collect(Collectors.toSet());
        return MenuDTO.builder()
                .id(menu.getId())
                .label(menu.getLabel())
                .path(menu.getPath())
                .parentId(menu.getParentId())
                .orden(menu.getOrden())
                .roles(roleNames)
                .build();
    }
}
