package com.libroclases.bff.service.asistencia;

import com.libroclases.bff.client.AsistenciaClient;
import com.libroclases.bff.dto.AlumnoDTO;
import com.libroclases.bff.dto.AnotacionDTO;
import com.libroclases.bff.dto.AsistenciaDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsistenciaQueryServiceTest {

    @Mock private AsistenciaClient client;
    @InjectMocks private AsistenciaQueryService service;

    @Test
    void listAlumnosDelegaEnElCliente() {
        List<AlumnoDTO> expected = List.of();
        when(client.listAlumnos()).thenReturn(expected);
        assertThat(service.listAlumnos()).isSameAs(expected);
    }

    @Test
    void getAlumnoDelegaEnElCliente() {
        AlumnoDTO expected = new AlumnoDTO();
        when(client.getAlumno(1L)).thenReturn(expected);
        assertThat(service.getAlumno(1L)).isSameAs(expected);
    }

    @Test
    void listByCursoDelegaEnElCliente() {
        List<AlumnoDTO> expected = List.of();
        when(client.listByCurso(3L)).thenReturn(expected);
        assertThat(service.listByCurso(3L)).isSameAs(expected);
    }

    @Test
    void historialAlumnoDelegaEnElCliente() {
        List<AsistenciaDTO> expected = List.of();
        when(client.historialAlumno(1L)).thenReturn(expected);
        assertThat(service.historialAlumno(1L)).isSameAs(expected);
    }

    @Test
    void porCursoYFechaDelegaEnElCliente() {
        List<AsistenciaDTO> expected = List.of();
        LocalDate fecha = LocalDate.of(2026, 6, 1);
        when(client.porCursoYFecha(3L, fecha)).thenReturn(expected);
        assertThat(service.porCursoYFecha(3L, fecha)).isSameAs(expected);
    }

    @Test
    void anotacionesAlumnoDelegaEnElCliente() {
        List<AnotacionDTO> expected = List.of();
        when(client.anotacionesAlumno(1L)).thenReturn(expected);
        assertThat(service.anotacionesAlumno(1L)).isSameAs(expected);
    }
}
