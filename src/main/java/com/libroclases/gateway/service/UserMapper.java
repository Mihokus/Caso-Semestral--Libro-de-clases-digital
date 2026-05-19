package com.libroclases.gateway.service;

import com.libroclases.gateway.dto.MenuDTO;
import com.libroclases.gateway.dto.UserDTO;
import com.libroclases.gateway.model.Menu;
import com.libroclases.gateway.model.Role;
import com.libroclases.gateway.model.User;
import com.libroclases.gateway.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final MenuRepository menuRepository;

    public UserDTO toDTO(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getNombre)
                .collect(Collectors.toSet());

        List<MenuDTO> menus = roleNames.isEmpty()
                ? List.of()
                : menuRepository.findDistinctByRoles_NombreIn(roleNames).stream()
                        .map(this::toMenuDTO)
                        .toList();

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nombre(user.getNombre())
                .entityId(user.getEntityId())
                .roles(roleNames)
                .menus(menus)
                .build();
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
