package com.libroclases.bff.service.asistencia;

import com.libroclases.bff.client.AsistenciaClient;
import com.libroclases.bff.client.dto.RawAnotacionRequest;
import com.libroclases.bff.client.dto.RawAsistenciaBulkRequest;
import com.libroclases.bff.dto.AnotacionDTO;
import com.libroclases.bff.dto.AnotacionRequest;
import com.libroclases.bff.dto.AsistenciaBulkRequest;
import com.libroclases.bff.dto.AsistenciaDTO;
import com.libroclases.bff.model.EstadoAsistencia;
import com.libroclases.bff.model.TipoAnotacion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsistenciaCommandServiceTest {

    @Mock private AsistenciaClient client;
    @InjectMocks private AsistenciaCommandService service;

    @Test
    void registrarBulkMapeaLasEntradasYIncluyeElRegistrador() {
        AsistenciaBulkRequest req = AsistenciaBulkRequest.builder()
                .cursoId(1L)
                .fecha(LocalDate.of(2026, 6, 1))
                .asistencias(List.of(
                        AsistenciaBulkRequest.Entrada.builder().alumnoId(10L).estado(EstadoAsistencia.PRESENTE).build()))
                .build();
        List<AsistenciaDTO> expected = List.of();
        when(client.registrarBulk(any(RawAsistenciaBulkRequest.class))).thenReturn(expected);

        List<AsistenciaDTO> result = service.registrarBulk(req, 5L, "Prof. García");

        assertThat(result).isSameAs(expected);
        ArgumentCaptor<RawAsistenciaBulkRequest> captor = ArgumentCaptor.forClass(RawAsistenciaBulkRequest.class);
        verify(client).registrarBulk(captor.capture());
        assertThat(captor.getValue().getRegistradoPorId()).isEqualTo(5L);
        assertThat(captor.getValue().getAsistencias()).hasSize(1);
    }

    @Test
    void registrarAnotacionDelegaEnElCliente() {
        AnotacionRequest req = new AnotacionRequest();
        req.setAlumnoId(10L);
        req.setTipo(TipoAnotacion.POSITIVA);
        req.setDescripcion("Buen trabajo");
        AnotacionDTO expected = new AnotacionDTO();
        when(client.registrarAnotacion(any(RawAnotacionRequest.class))).thenReturn(expected);

        assertThat(service.registrarAnotacion(req, 5L, "Prof. García")).isSameAs(expected);
    }
}
