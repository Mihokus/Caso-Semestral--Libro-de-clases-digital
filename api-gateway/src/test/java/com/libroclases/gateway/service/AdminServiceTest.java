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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private MenuRepository menuRepository;
    @Mock private UserMapper userMapper;

    @InjectMocks private AdminService adminService;

    private User user() {
        return User.builder().id(1L).email("ana@colegio.cl").nombre("Ana").build();
    }

    // ---- Users ----

    @Test
    void listUsersMapeaLasEntidadesADTO() {
        when(userRepository.findAll()).thenReturn(List.of(user()));
        when(userMapper.toDTO(any(User.class)))
                .thenReturn(UserDTO.builder().id(1L).email("ana@colegio.cl").build());

        List<UserDTO> result = adminService.listUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("ana@colegio.cl");
    }

    @Test
    void updateUserRolesActualizaLosRolesDelUsuario() {
        UpdateRolesRequest req = new UpdateRolesRequest();
        req.setRoleIds(Set.of(1L));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user()));
        when(roleRepository.findAllById(Set.of(1L)))
                .thenReturn(List.of(Role.builder().id(1L).nombre("DOCENTE").build()));
        when(userMapper.toDTO(any(User.class))).thenReturn(UserDTO.builder().id(1L).build());

        UserDTO dto = adminService.updateUserRoles(1L, req);

        assertThat(dto.getId()).isEqualTo(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserRolesFallaSiElUsuarioNoExiste() {
        UpdateRolesRequest req = new UpdateRolesRequest();
        req.setRoleIds(Set.of(1L));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminService.updateUserRoles(99L, req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateUserRolesFallaSiAlgunRolNoExiste() {
        UpdateRolesRequest req = new UpdateRolesRequest();
        req.setRoleIds(Set.of(1L, 2L));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user()));
        when(roleRepository.findAllById(Set.of(1L, 2L)))
                .thenReturn(List.of(Role.builder().id(1L).nombre("DOCENTE").build()));

        assertThatThrownBy(() -> adminService.updateUserRoles(1L, req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteUserEliminaCuandoExiste() {
        when(userRepository.existsById(1L)).thenReturn(true);

        adminService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUserFallaSiNoExiste() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> adminService.deleteUser(99L))
                .isInstanceOf(IllegalArgumentException.class);
        verify(userRepository, never()).deleteById(any());
    }

    // ---- Roles ----

    @Test
    void listRolesDevuelveLosRoles() {
        when(roleRepository.findAll())
                .thenReturn(List.of(Role.builder().id(1L).nombre("DOCENTE").descripcion("Profe").build()));

        List<RoleDTO> result = adminService.listRoles();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("DOCENTE");
    }

    @Test
    void createRoleCreaUnRolNuevo() {
        RoleDTO req = RoleDTO.builder().nombre("APODERADO").descripcion("Apoderado").build();
        when(roleRepository.existsByNombre("APODERADO")).thenReturn(false);
        when(roleRepository.save(any(Role.class)))
                .thenReturn(Role.builder().id(5L).nombre("APODERADO").descripcion("Apoderado").build());

        RoleDTO dto = adminService.createRole(req);

        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getNombre()).isEqualTo("APODERADO");
    }

    @Test
    void createRoleFallaSiYaExiste() {
        RoleDTO req = RoleDTO.builder().nombre("DOCENTE").build();
        when(roleRepository.existsByNombre("DOCENTE")).thenReturn(true);

        assertThatThrownBy(() -> adminService.createRole(req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ---- Menus ----

    @Test
    void listMenusDevuelveLosMenus() {
        Menu menu = Menu.builder().id(1L).label("Dashboard").path("/dashboard")
                .roles(Set.of(Role.builder().nombre("DOCENTE").build())).build();
        when(menuRepository.findAll()).thenReturn(List.of(menu));

        List<MenuDTO> result = adminService.listMenus();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLabel()).isEqualTo("Dashboard");
        assertThat(result.get(0).getRoles()).containsExactly("DOCENTE");
    }

    @Test
    void createMenuConRolesAsociaLosRoles() {
        MenuRequest req = new MenuRequest();
        req.setLabel("Asistencia");
        req.setPath("/asistencia");
        req.setOrden(2);
        req.setRoleIds(Set.of(1L));
        when(roleRepository.findAllById(Set.of(1L)))
                .thenReturn(List.of(Role.builder().id(1L).nombre("DOCENTE").build()));
        when(menuRepository.save(any(Menu.class)))
                .thenReturn(Menu.builder().id(3L).label("Asistencia").path("/asistencia")
                        .roles(Set.of(Role.builder().nombre("DOCENTE").build())).build());

        MenuDTO dto = adminService.createMenu(req);

        assertThat(dto.getLabel()).isEqualTo("Asistencia");
    }

    @Test
    void createMenuSinRolesUsaConjuntoVacio() {
        MenuRequest req = new MenuRequest();
        req.setLabel("Inicio");
        req.setPath("/inicio");
        req.setRoleIds(null);
        when(menuRepository.save(any(Menu.class)))
                .thenReturn(Menu.builder().id(4L).label("Inicio").path("/inicio").roles(Set.of()).build());

        MenuDTO dto = adminService.createMenu(req);

        assertThat(dto.getLabel()).isEqualTo("Inicio");
        assertThat(dto.getRoles()).isEmpty();
        verify(roleRepository, never()).findAllById(any());
    }

    @Test
    void updateMenuRolesActualizaLosRolesDelMenu() {
        UpdateRolesRequest req = new UpdateRolesRequest();
        req.setRoleIds(Set.of(1L));
        Menu menu = Menu.builder().id(1L).label("Dashboard").path("/dashboard").build();
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(roleRepository.findAllById(Set.of(1L)))
                .thenReturn(List.of(Role.builder().id(1L).nombre("ADMIN").build()));

        MenuDTO dto = adminService.updateMenuRoles(1L, req);

        assertThat(dto.getRoles()).containsExactly("ADMIN");
        verify(menuRepository).save(menu);
    }

    @Test
    void updateMenuRolesFallaSiElMenuNoExiste() {
        UpdateRolesRequest req = new UpdateRolesRequest();
        req.setRoleIds(Set.of(1L));
        when(menuRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminService.updateMenuRoles(99L, req))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
