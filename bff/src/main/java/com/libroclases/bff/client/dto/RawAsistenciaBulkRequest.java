package com.libroclases.bff.client.dto;

import com.libroclases.bff.model.EstadoAsistencia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawAsistenciaBulkRequest {
    private Long cursoId;
    private LocalDate fecha;
    private Long registradoPorId;
    private String registradoPorNombre;
    private List<Entrada> asistencias;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Entrada {
        private Long alumnoId;
        private EstadoAsistencia estado;
    }
}
