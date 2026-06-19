package com.libroclases.bff.controller;

import com.libroclases.bff.dto.AlumnoDTO;
import com.libroclases.bff.dto.AnotacionDTO;
import com.libroclases.bff.dto.AnotacionRequest;
import com.libroclases.bff.dto.AsistenciaBulkRequest;
import com.libroclases.bff.dto.AsistenciaDTO;
import com.libroclases.bff.service.asistencia.AsistenciaCommandService;
import com.libroclases.bff.service.asistencia.AsistenciaQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsistenciaControllerTest {

    @Mock private AsistenciaQueryService queryService;
    @Mock private AsistenciaCommandService commandService;
    @InjectMocks private AsistenciaController controller;

    @Test
    void listAlumnosDelega() {
        List<AlumnoDTO> expected = List.of();
        when(queryService.listAlumnos()).thenReturn(expected);
        assertThat(controller.listAlumnos()).isSameAs(expected);
    }

    @Test
    void getAlumnoDelega() {
        AlumnoDTO expected = new AlumnoDTO();
        when(queryService.getAlumno(1L)).thenReturn(expected);
        assertThat(controller.getAlumno(1L)).isSameAs(expected);
    }

    @Test
    void listByCursoDelega() {
        List<AlumnoDTO> expected = List.of();
        when(queryService.listByCurso(3L)).thenReturn(expected);
        assertThat(controller.listByCurso(3L)).isSameAs(expected);
    }

    @Test
    void registrarBulkDevuelve201() {
        AsistenciaBulkRequest req = AsistenciaBulkRequest.builder().build();
        List<AsistenciaDTO> dto = List.of();
        when(commandService.registrarBulk(req, 5L, "Prof")).thenReturn(dto);

        ResponseEntity<List<AsistenciaDTO>> res = controller.registrarBulk(req, 5L, "Prof");

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void historialAlumnoDelega() {
        List<AsistenciaDTO> expected = List.of();
        when(queryService.historialAlumno(1L)).thenReturn(expected);
        assertThat(controller.historialAlumno(1L)).isSameAs(expected);
    }

    @Test
    void porCursoYFechaDelega() {
        List<AsistenciaDTO> expected = List.of();
        LocalDate fecha = LocalDate.of(2026, 6, 1);
        when(queryService.porCursoYFecha(3L, fecha)).thenReturn(expected);
        assertThat(controller.porCursoYFecha(3L, fecha)).isSameAs(expected);
    }

    @Test
    void registrarAnotacionDevuelve201() {
        AnotacionRequest req = new AnotacionRequest();
        AnotacionDTO dto = new AnotacionDTO();
        when(commandService.registrarAnotacion(req, 5L, "Prof")).thenReturn(dto);

        ResponseEntity<AnotacionDTO> res = controller.registrarAnotacion(req, 5L, "Prof");

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void anotacionesAlumnoDelega() {
        List<AnotacionDTO> expected = List.of();
        when(queryService.anotacionesAlumno(1L)).thenReturn(expected);
        assertThat(controller.anotacionesAlumno(1L)).isSameAs(expected);
    }
}
