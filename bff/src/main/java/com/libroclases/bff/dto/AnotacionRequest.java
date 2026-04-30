package com.libroclases.bff.dto;

import com.libroclases.bff.model.TipoAnotacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnotacionRequest {

    @NotNull
    private Long alumnoId;

    @NotNull
    private TipoAnotacion tipo;

    @NotBlank
    private String descripcion;
}
