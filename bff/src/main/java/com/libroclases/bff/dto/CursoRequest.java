package com.libroclases.bff.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CursoRequest {

    @NotBlank
    private String nombre;

    @NotBlank
    private String nivel;
}
