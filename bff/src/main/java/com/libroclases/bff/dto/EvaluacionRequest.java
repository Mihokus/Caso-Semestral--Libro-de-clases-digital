package com.libroclases.bff.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EvaluacionRequest {

    @NotNull
    private Long alumnoId;

    @NotNull
    private Long asignaturaId;

    @NotBlank
    private String nombre;

    @NotNull
    @DecimalMin("1.0")
    @DecimalMax("7.0")
    private Double nota;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private Double ponderacion;
}
