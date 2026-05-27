package com.libroclases.bff.dto;

import com.libroclases.bff.model.EstadoAsistencia;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class AsistenciaBulkRequest {

    @NotNull
    private Long cursoId;

    @NotNull
    private LocalDate fecha;

    @NotEmpty
    private List<Entrada> asistencias;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Entrada {
        @NotNull
        private Long alumnoId;
        @NotNull
        private EstadoAsistencia estado;
    }
}
