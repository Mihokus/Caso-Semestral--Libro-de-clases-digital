package com.libroclases.bff.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsignaturaRequest {

    @NotBlank
    private String nombre;

    @NotNull
    private Long cursoId;

    @NotNull
    private Long docenteId;
}
