package com.libroclases.bff.service.academica;

import com.libroclases.bff.client.AcademicaClient;
import com.libroclases.bff.dto.AsignaturaDTO;
import com.libroclases.bff.dto.CursoDTO;
import com.libroclases.bff.dto.EvaluacionDTO;
import com.libroclases.bff.dto.RendimientoDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcademicaQueryServiceTest {

    @Mock private AcademicaClient client;
    @InjectMocks private AcademicaQueryService service;

    @Test
    void listAsignaturasDelegaEnElCliente() {
        List<AsignaturaDTO> expected = List.of();
        when(client.listAsignaturas()).thenReturn(expected);
        assertThat(service.listAsignaturas()).isSameAs(expected);
    }

    @Test
    void getAsignaturaDelegaEnElCliente() {
        AsignaturaDTO expected = new AsignaturaDTO();
        when(client.getAsignatura(1L)).thenReturn(expected);
        assertThat(service.getAsignatura(1L)).isSameAs(expected);
    }

    @Test
    void rendimientoDelegaEnElCliente() {
        RendimientoDTO expected = new RendimientoDTO();
        when(client.rendimiento(1L)).thenReturn(expected);
        assertThat(service.rendimiento(1L)).isSameAs(expected);
    }

    @Test
    void listCursosDelegaEnElCliente() {
        List<CursoDTO> expected = List.of();
        when(client.listCursos()).thenReturn(expected);
        assertThat(service.listCursos()).isSameAs(expected);
    }

    @Test
    void notasAlumnoDelegaEnElCliente() {
        List<EvaluacionDTO> expected = List.of();
        when(client.notasAlumno(7L)).thenReturn(expected);
        assertThat(service.notasAlumno(7L)).isSameAs(expected);
    }
}
