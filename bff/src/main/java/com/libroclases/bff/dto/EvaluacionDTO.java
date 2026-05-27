package com.libroclases.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluacionDTO {
    private Long id;
    private Long alumnoId;
    private String alumnoNombre;
    private Long asignaturaId;
    private String asignaturaNombre;
    private String nombre;
    private Double nota;
    private Double ponderacion;
    private LocalDate fecha;
    private RegistradoPor registradoPor;
}
