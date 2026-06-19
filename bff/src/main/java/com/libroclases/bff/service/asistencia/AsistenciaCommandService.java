package com.libroclases.bff.service.asistencia;

import com.libroclases.bff.client.AsistenciaClient;
import com.libroclases.bff.client.dto.RawAnotacionRequest;
import com.libroclases.bff.client.dto.RawAsistenciaBulkRequest;
import com.libroclases.bff.dto.AnotacionDTO;
import com.libroclases.bff.dto.AnotacionRequest;
import com.libroclases.bff.dto.AsistenciaBulkRequest;
import com.libroclases.bff.dto.AsistenciaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsistenciaCommandService {

    private final AsistenciaClient client;

    public List<AsistenciaDTO> registrarBulk(AsistenciaBulkRequest req, Long userId, String userName) {
        RawAsistenciaBulkRequest raw = RawAsistenciaBulkRequest.builder()
                .cursoId(req.getCursoId())
                .fecha(req.getFecha())
                .registradoPorId(userId)
                .registradoPorNombre(userName)
                .asistencias(req.getAsistencias().stream()
                        .map(e -> RawAsistenciaBulkRequest.Entrada.builder()
                                .alumnoId(e.getAlumnoId())
                                .estado(e.getEstado())
                                .build())
                        .toList())
                .build();
        return client.registrarBulk(raw);
    }

    public AnotacionDTO registrarAnotacion(AnotacionRequest req, Long userId, String userName) {
        RawAnotacionRequest raw = RawAnotacionRequest.builder()
                .alumnoId(req.getAlumnoId())
                .tipo(req.getTipo())
                .descripcion(req.getDescripcion())
                .registradoPorId(userId)
                .registradoPorNombre(userName)
                .build();
        return client.registrarAnotacion(raw);
    }
}
