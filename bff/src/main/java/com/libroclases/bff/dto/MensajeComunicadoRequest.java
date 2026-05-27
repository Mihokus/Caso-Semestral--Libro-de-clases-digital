package com.libroclases.bff.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MensajeComunicadoRequest {

    @NotNull
    private Long cursoId;

    @NotBlank
    private String titulo;

    @NotBlank
    private String contenido;
}
