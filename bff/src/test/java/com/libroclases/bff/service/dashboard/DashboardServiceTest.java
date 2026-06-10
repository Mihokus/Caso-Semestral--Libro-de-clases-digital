package com.libroclases.bff.service.dashboard;

import com.libroclases.bff.dto.AlumnoDTO;
import com.libroclases.bff.dto.AsignaturaDTO;
import com.libroclases.bff.dto.CursoDTO;
import com.libroclases.bff.dto.MensajeDTO;
import com.libroclases.bff.dto.dashboard.AdminDashboardDTO;
import com.libroclases.bff.dto.dashboard.ApoderadoDashboardDTO;
import com.libroclases.bff.dto.dashboard.DashboardDTO;
import com.libroclases.bff.dto.dashboard.DocenteDashboardDTO;
import com.libroclases.bff.dto.dashboard.EstudianteDashboardDTO;
import com.libroclases.bff.service.academica.AcademicaQueryService;
import com.libroclases.bff.service.asistencia.AsistenciaQueryService;
import com.libroclases.bff.service.mensajeria.MensajeriaQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock private AsistenciaQueryService asistenciaQuery;
    @Mock private MensajeriaQueryService mensajeriaQuery;
    @Mock private AcademicaQueryService academicaQuery;

    private DashboardService service;

    @BeforeEach
    void setUp() {
        service = new DashboardService(asistenciaQuery, mensajeriaQuery, academicaQuery, new DashboardDTOFactory());
    }

    @Test
    void buildDocenteCuentaLosMensajesNoLeidos() {
        when(mensajeriaQuery.inbox(1L)).thenReturn(List.of(
                MensajeDTO.builder().id(1L).leido(false).build(),
                MensajeDTO.builder().id(2L).leido(true).build(),
                MensajeDTO.builder().id(3L).leido(false).build()));
        when(academicaQuery.listAsignaturas()).thenReturn(List.<AsignaturaDTO>of());

        DashboardDTO dto = service.build(1L, "Ana", "ana@colegio.cl", "DOCENTE", null);

        assertThat(dto).isInstanceOf(DocenteDashboardDTO.class);
        assertThat(((DocenteDashboardDTO) dto).getMensajesNoLeidos()).isEqualTo(2);
    }

    @Test
    void buildAdminCuentaCursosYAsignaturas() {
        when(academicaQuery.listAsignaturas())
                .thenReturn(List.of(new AsignaturaDTO(), new AsignaturaDTO()));
        when(academicaQuery.listCursos())
                .thenReturn(List.of(new CursoDTO(), new CursoDTO(), new CursoDTO()));

        DashboardDTO dto = service.build(1L, "Admin", "admin@colegio.cl", "ADMIN", null);

        AdminDashboardDTO a = (AdminDashboardDTO) dto;
        assertThat(a.getTotalAsignaturas()).isEqualTo(2);
        assertThat(a.getTotalCursos()).isEqualTo(3);
    }

    @Test
    void buildEstudianteResuelveElAlumnoPorEntityId() {
        when(asistenciaQuery.getAlumno(5L))
                .thenReturn(AlumnoDTO.builder().id(5L).nombre("Pedro").build());

        DashboardDTO dto = service.build(1L, "Pedro", "pedro@colegio.cl", "ESTUDIANTE", 5L);

        EstudianteDashboardDTO e = (EstudianteDashboardDTO) dto;
        assertThat(e.getAlumno()).isNotNull();
        assertThat(e.getAlumno().getNombre()).isEqualTo("Pedro");
    }

    @Test
    void buildApoderadoDevuelveDashboardDeApoderado() {
        DashboardDTO dto = service.build(1L, "Apo", "apo@colegio.cl", "APODERADO", null);

        assertThat(dto).isInstanceOf(ApoderadoDashboardDTO.class);
    }

    @Test
    void buildLanzaExcepcionConRolDesconocido() {
        assertThatThrownBy(() -> service.build(1L, "X", "x@colegio.cl", "INVITADO", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ---- Circuit breaker ----

    @Test
    void circuitBreakerDevuelveListaVaciaSiAcademicaFalla() {
        when(academicaQuery.listAsignaturas()).thenThrow(new RuntimeException("academica caída"));

        DashboardDTO dto = service.build(1L, "Ana", "ana@colegio.cl", "DOCENTE", null);

        assertThat(((DocenteDashboardDTO) dto).getAsignaturas()).isEmpty();
    }

    @Test
    void circuitBreakerDevuelveCeroSiElConteoFalla() {
        when(academicaQuery.listAsignaturas()).thenThrow(new RuntimeException("academica caída"));

        DashboardDTO dto = service.build(1L, "Admin", "admin@colegio.cl", "ADMIN", null);

        assertThat(((AdminDashboardDTO) dto).getTotalAsignaturas()).isZero();
    }

    @Test
    void circuitBreakerDevuelveCeroNoLeidosSiMensajeriaFalla() {
        when(mensajeriaQuery.inbox(1L)).thenThrow(new RuntimeException("mensajeria caída"));

        DashboardDTO dto = service.build(1L, "Ana", "ana@colegio.cl", "DOCENTE", null);

        assertThat(((DocenteDashboardDTO) dto).getMensajesNoLeidos()).isZero();
    }

    @Test
    void buildEstudianteToleraFalloAlResolverAlumno() {
        when(asistenciaQuery.getAlumno(5L)).thenThrow(new RuntimeException("asistencia caída"));

        DashboardDTO dto = service.build(1L, "Pedro", "pedro@colegio.cl", "ESTUDIANTE", 5L);

        assertThat(((EstudianteDashboardDTO) dto).getAlumno()).isNull();
    }
}
