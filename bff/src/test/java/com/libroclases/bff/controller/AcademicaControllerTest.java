package com.libroclases.bff.controller;

import com.libroclases.bff.dto.AsignaturaDTO;
import com.libroclases.bff.dto.AsignaturaRequest;
import com.libroclases.bff.dto.CursoDTO;
import com.libroclases.bff.dto.CursoRequest;
import com.libroclases.bff.dto.EvaluacionDTO;
import com.libroclases.bff.dto.EvaluacionRequest;
import com.libroclases.bff.dto.RendimientoDTO;
import com.libroclases.bff.service.academica.AcademicaCommandService;
import com.libroclases.bff.service.academica.AcademicaQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcademicaControllerTest {

    @Mock private AcademicaQueryService queryService;
    @Mock private AcademicaCommandService commandService;
    @InjectMocks private AcademicaController controller;

    @Test
    void listAsignaturasDelega() {
        List<AsignaturaDTO> expected = List.of();
        when(queryService.listAsignaturas()).thenReturn(expected);
        assertThat(controller.listAsignaturas()).isSameAs(expected);
    }

    @Test
    void getAsignaturaDelega() {
        AsignaturaDTO expected = new AsignaturaDTO();
        when(queryService.getAsignatura(1L)).thenReturn(expected);
        assertThat(controller.getAsignatura(1L)).isSameAs(expected);
    }

    @Test
    void crearAsignaturaDevuelve201() {
        AsignaturaRequest req = new AsignaturaRequest();
        AsignaturaDTO dto = new AsignaturaDTO();
        when(commandService.crearAsignatura(req, "Prof")).thenReturn(dto);

        ResponseEntity<AsignaturaDTO> res = controller.crearAsignatura(req, "Prof");

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).isSameAs(dto);
    }

    @Test
    void rendimientoDelega() {
        RendimientoDTO expected = new RendimientoDTO();
        when(queryService.rendimiento(1L)).thenReturn(expected);
        assertThat(controller.rendimiento(1L)).isSameAs(expected);
    }

    @Test
    void listCursosDelega() {
        List<CursoDTO> expected = List.of();
        when(queryService.listCursos()).thenReturn(expected);
        assertThat(controller.listCursos()).isSameAs(expected);
    }

    @Test
    void crearCursoDevuelve201() {
        CursoRequest req = new CursoRequest();
        req.setNombre("8°A");
        req.setNivel("Básica");
        CursoDTO dto = new CursoDTO();
        when(commandService.crearCurso("8°A", "Básica")).thenReturn(dto);

        ResponseEntity<CursoDTO> res = controller.crearCurso(req);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void registrarNotaDevuelve201() {
        EvaluacionRequest req = new EvaluacionRequest();
        EvaluacionDTO dto = new EvaluacionDTO();
        when(commandService.registrarNota(req, 5L, "Prof")).thenReturn(dto);

        ResponseEntity<EvaluacionDTO> res = controller.registrarNota(req, 5L, "Prof");

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void notasAlumnoDelega() {
        List<EvaluacionDTO> expected = List.of();
        when(queryService.notasAlumno(7L)).thenReturn(expected);
        assertThat(controller.notasAlumno(7L)).isSameAs(expected);
    }
}
