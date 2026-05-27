package com.libroclases.bff.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawMensajeComunicadoRequest {
    private Long cursoId;
    private String titulo;
    private String contenido;
    private Long remitenteId;
    private String remitenteNombre;
    private String remitenteRol;
}
