package com.microservicio.mensajeria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajeRequest {
    private String titulo;
    private String contenido;

    private Long remitenteId;
    private String remitenteNombre;
    private String remitenteRol;

    private Long destinatarioId;
    private String destinatarioNombre;
    private String destinatarioRol;

    private Long cursoId;
}
