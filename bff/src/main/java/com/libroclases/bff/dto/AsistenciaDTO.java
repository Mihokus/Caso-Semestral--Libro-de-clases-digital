package com.libroclases.bff.dto;

import com.libroclases.bff.model.EstadoAsistencia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaDTO {
    private Long id;
    private Long alumnoId;
    private String alumnoNombre;
    private Long cursoId;
    private String cursoNombre;
    private LocalDate fecha;
    private EstadoAsistencia estado;
    private RegistradoPor registradoPor;
}
