package com.libroclases.bff.dto;

import com.libroclases.bff.model.TipoAnotacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnotacionDTO {
    private Long id;
    private Long alumnoId;
    private String alumnoNombre;
    private TipoAnotacion tipo;
    private String descripcion;
    private Instant fecha;
    private RegistradoPor registradoPor;
}
