package com.libroclases.bff.service.academica;

import com.libroclases.bff.client.AcademicaClient;
import com.libroclases.bff.client.dto.RawAsignaturaRequest;
import com.libroclases.bff.client.dto.RawCursoRequest;
import com.libroclases.bff.client.dto.RawEvaluacionRequest;
import com.libroclases.bff.dto.AsignaturaDTO;
import com.libroclases.bff.dto.AsignaturaRequest;
import com.libroclases.bff.dto.CursoDTO;
import com.libroclases.bff.dto.EvaluacionDTO;
import com.libroclases.bff.dto.EvaluacionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcademicaCommandServiceTest {

    @Mock private AcademicaClient client;
    @InjectMocks private AcademicaCommandService service;

    @Test
    void crearAsignaturaArmaElRawRequestConElDocente() {
        AsignaturaRequest req = new AsignaturaRequest();
        req.setNombre("Matemáticas");
        req.setCursoId(1L);
        req.setDocenteId(2L);
        AsignaturaDTO expected = new AsignaturaDTO();
        when(client.crearAsignatura(any(RawAsignaturaRequest.class))).thenReturn(expected);

        AsignaturaDTO result = service.crearAsignatura(req, "Prof. García");

        assertThat(result).isSameAs(expected);
        ArgumentCaptor<RawAsignaturaRequest> captor = ArgumentCaptor.forClass(RawAsignaturaRequest.class);
        verify(client).crearAsignatura(captor.capture());
        assertThat(captor.getValue().getDocenteNombre()).isEqualTo("Prof. García");
    }

    @Test
    void crearCursoDelegaEnElCliente() {
        CursoDTO expected = new CursoDTO();
        when(client.crearCurso(any(RawCursoRequest.class))).thenReturn(expected);

        assertThat(service.crearCurso("8°A", "Básica")).isSameAs(expected);
    }

    @Test
    void registrarNotaIncluyeElRegistrador() {
        EvaluacionRequest req = new EvaluacionRequest();
        req.setAlumnoId(1L);
        req.setAsignaturaId(1L);
        req.setNombre("Prueba 1");
        req.setNota(6.0);
        req.setPonderacion(0.3);
        EvaluacionDTO expected = new EvaluacionDTO();
        when(client.registrarNota(any(RawEvaluacionRequest.class))).thenReturn(expected);

        EvaluacionDTO result = service.registrarNota(req, 5L, "Prof. García");

        assertThat(result).isSameAs(expected);
        ArgumentCaptor<RawEvaluacionRequest> captor = ArgumentCaptor.forClass(RawEvaluacionRequest.class);
        verify(client).registrarNota(captor.capture());
        assertThat(captor.getValue().getRegistradoPorId()).isEqualTo(5L);
    }
}
