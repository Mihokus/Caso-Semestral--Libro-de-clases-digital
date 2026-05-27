package com.libroclases.bff.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MensajeDirectoRequest {

    @NotNull
    private Long destinatarioId;

    @NotBlank
    private String titulo;

    @NotBlank
    private String contenido;
}
