package com.libroclases.bff.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawEvaluacionRequest {
    private Long alumnoId;
    private Long asignaturaId;
    private String nombre;
    private Double nota;
    private Double ponderacion;
    private Long registradoPorId;
    private String registradoPorNombre;
}
