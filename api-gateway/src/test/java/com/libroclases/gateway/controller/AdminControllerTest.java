package com.libroclases.gateway.controller;

import com.libroclases.gateway.dto.MenuDTO;
import com.libroclases.gateway.dto.MenuRequest;
import com.libroclases.gateway.dto.RoleDTO;
import com.libroclases.gateway.dto.UpdateRolesRequest;
import com.libroclases.gateway.dto.UserDTO;
import com.libroclases.gateway.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock private AdminService adminService;

    @InjectMocks private AdminController controller;

    @Test
    void listUsersDelegaEnElServicio() {
        when(adminService.listUsers()).thenReturn(List.of(UserDTO.builder().id(1L).build()));

        List<UserDTO> result = controller.listUsers();

        assertThat(result).hasSize(1);
    }

    @Test
    void updateUserRolesDelegaEnElServicio() {
        UpdateRolesRequest req = new UpdateRolesRequest();
        when(adminService.updateUserRoles(eq(1L), any())).thenReturn(UserDTO.builder().id(1L).build());

        UserDTO dto = controller.updateUserRoles(1L, req);

        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    void deleteUserDevuelve204() {
        ResponseEntity<Void> res = controller.deleteUser(1L);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(adminService).deleteUser(1L);
    }

    @Test
    void listRolesDelegaEnElServicio() {
        when(adminService.listRoles()).thenReturn(List.of(RoleDTO.builder().nombre("DOCENTE").build()));

        List<RoleDTO> result = controller.listRoles();

        assertThat(result).hasSize(1);
    }

    @Test
    void createRoleDevuelve201() {
        RoleDTO req = RoleDTO.builder().nombre("APODERADO").build();
        when(adminService.createRole(req)).thenReturn(RoleDTO.builder().id(5L).nombre("APODERADO").build());

        ResponseEntity<RoleDTO> res = controller.createRole(req);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().getId()).isEqualTo(5L);
    }

    @Test
    void listMenusDelegaEnElServicio() {
        when(adminService.listMenus()).thenReturn(List.of(MenuDTO.builder().label("Dashboard").build()));

        List<MenuDTO> result = controller.listMenus();

        assertThat(result).hasSize(1);
    }

    @Test
    void createMenuDevuelve201() {
        MenuRequest req = new MenuRequest();
        when(adminService.createMenu(req)).thenReturn(MenuDTO.builder().id(3L).label("Asistencia").build());

        ResponseEntity<MenuDTO> res = controller.createMenu(req);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().getLabel()).isEqualTo("Asistencia");
    }

    @Test
    void updateMenuRolesDelegaEnElServicio() {
        UpdateRolesRequest req = new UpdateRolesRequest();
        when(adminService.updateMenuRoles(eq(1L), any())).thenReturn(MenuDTO.builder().id(1L).build());

        MenuDTO dto = controller.updateMenuRoles(1L, req);

        assertThat(dto.getId()).isEqualTo(1L);
    }
}
