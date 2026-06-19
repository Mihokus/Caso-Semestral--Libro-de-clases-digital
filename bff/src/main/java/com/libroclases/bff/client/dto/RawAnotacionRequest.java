package com.libroclases.bff.client.dto;

import com.libroclases.bff.model.TipoAnotacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawAnotacionRequest {
    private Long alumnoId;
    private TipoAnotacion tipo;
    private String descripcion;
    private Long registradoPorId;
    private String registradoPorNombre;
}
