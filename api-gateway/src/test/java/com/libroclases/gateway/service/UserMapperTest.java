package com.libroclases.gateway.service;

import com.libroclases.gateway.dto.UserDTO;
import com.libroclases.gateway.model.Menu;
import com.libroclases.gateway.model.Role;
import com.libroclases.gateway.model.User;
import com.libroclases.gateway.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock private MenuRepository menuRepository;

    @InjectMocks private UserMapper userMapper;

    @Test
    void toDTOIncluyeLosMenusDeLosRolesDelUsuario() {
        User user = User.builder()
                .id(1L).email("ana@colegio.cl").nombre("Ana")
                .roles(Set.of(Role.builder().id(1L).nombre("DOCENTE").build()))
                .build();
        Menu menu = Menu.builder()
                .id(1L).label("Dashboard").path("/dashboard")
                .roles(Set.of(Role.builder().nombre("DOCENTE").build()))
                .build();
        when(menuRepository.findDistinctByRoles_NombreIn(any())).thenReturn(List.of(menu));

        UserDTO dto = userMapper.toDTO(user);

        assertThat(dto.getEmail()).isEqualTo("ana@colegio.cl");
        assertThat(dto.getRoles()).containsExactly("DOCENTE");
        assertThat(dto.getMenus()).hasSize(1);
        assertThat(dto.getMenus().get(0).getLabel()).isEqualTo("Dashboard");
    }

    @Test
    void toDTONoConsultaMenusCuandoElUsuarioNoTieneRoles() {
        User user = User.builder()
                .id(2L).email("sin@colegio.cl").nombre("Sin Roles")
                .roles(Set.of())
                .build();

        UserDTO dto = userMapper.toDTO(user);

        assertThat(dto.getRoles()).isEmpty();
        assertThat(dto.getMenus()).isEmpty();
        verify(menuRepository, never()).findDistinctByRoles_NombreIn(any());
    }
}
