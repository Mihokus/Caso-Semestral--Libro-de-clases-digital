package com.libroclases.bff.service.dashboard;

import com.libroclases.bff.dto.AlumnoDTO;
import com.libroclases.bff.dto.dashboard.AdminDashboardDTO;
import com.libroclases.bff.dto.dashboard.ApoderadoDashboardDTO;
import com.libroclases.bff.dto.dashboard.DashboardDTO;
import com.libroclases.bff.dto.dashboard.DocenteDashboardDTO;
import com.libroclases.bff.dto.dashboard.EstudianteDashboardDTO;
import com.libroclases.bff.dto.dashboard.UserSummary;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DashboardDTOFactoryTest {

    private final DashboardDTOFactory factory = new DashboardDTOFactory();
    private final UserSummary user = UserSummary.builder().id(1L).email("ana@colegio.cl").nombre("Ana").build();

    @Test
    void creaUnDashboardDeDocente() {
        DashboardContext ctx = DashboardContext.builder()
                .user(user).asignaturas(List.of()).alumnosACargo(30)
                .mensajesNoLeidos(2).evaluacionesPendientes(1).build();

        DashboardDTO dto = factory.create("DOCENTE", ctx);

        assertThat(dto).isInstanceOf(DocenteDashboardDTO.class);
        assertThat(dto.getRol()).isEqualTo("DOCENTE");
        DocenteDashboardDTO d = (DocenteDashboardDTO) dto;
        assertThat(d.getAlumnosACargo()).isEqualTo(30);
        assertThat(d.getEvaluacionesPendientes()).isEqualTo(1);
    }

    @Test
    void creaUnDashboardDeApoderado() {
        DashboardContext ctx = DashboardContext.builder()
                .user(user).pupilos(List.of()).mensajesNoLeidos(0).build();

        DashboardDTO dto = factory.create("APODERADO", ctx);

        assertThat(dto).isInstanceOf(ApoderadoDashboardDTO.class);
        assertThat(dto.getRol()).isEqualTo("APODERADO");
    }

    @Test
    void creaUnDashboardDeEstudiante() {
        DashboardContext ctx = DashboardContext.builder()
                .user(user)
                .alumno(AlumnoDTO.builder().id(1L).nombre("Ana").build())
                .asistenciaPorcentaje(95.0).promedioGeneral(6.0).mensajesNoLeidos(0).build();

        DashboardDTO dto = factory.create("ESTUDIANTE", ctx);

        assertThat(dto).isInstanceOf(EstudianteDashboardDTO.class);
        assertThat(((EstudianteDashboardDTO) dto).getPromedioGeneral()).isEqualTo(6.0);
    }

    @Test
    void creaUnDashboardDeAdmin() {
        DashboardContext ctx = DashboardContext.builder()
                .user(user).totalUsuarios(10).totalCursos(5).totalAsignaturas(8).mensajesNoLeidos(0).build();

        DashboardDTO dto = factory.create("ADMIN", ctx);

        assertThat(dto).isInstanceOf(AdminDashboardDTO.class);
        assertThat(((AdminDashboardDTO) dto).getTotalUsuarios()).isEqualTo(10);
    }

    @Test
    void aceptaElRolEnMinusculas() {
        DashboardContext ctx = DashboardContext.builder().user(user).build();

        assertThat(factory.create("docente", ctx)).isInstanceOf(DocenteDashboardDTO.class);
    }

    @Test
    void lanzaExcepcionConRolNull() {
        DashboardContext ctx = DashboardContext.builder().user(user).build();

        assertThatThrownBy(() -> factory.create(null, ctx))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void lanzaExcepcionConRolDesconocido() {
        DashboardContext ctx = DashboardContext.builder().user(user).build();

        assertThatThrownBy(() -> factory.create("SUPERADMIN", ctx))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void usaValoresPorDefectoCuandoElContextoTieneCamposNull() {
        DashboardContext ctx = DashboardContext.builder().user(user).build();

        DocenteDashboardDTO d = (DocenteDashboardDTO) factory.create("DOCENTE", ctx);

        assertThat(d.getAsignaturas()).isEmpty();
        assertThat(d.getAlumnosACargo()).isZero();
        assertThat(d.getMensajesNoLeidos()).isZero();
        assertThat(d.getEvaluacionesPendientes()).isZero();
    }
}
