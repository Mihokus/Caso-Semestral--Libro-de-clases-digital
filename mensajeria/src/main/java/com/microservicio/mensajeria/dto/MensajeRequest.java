package com.microservicio.mensajeria.dto;

import com.microservicio.mensajeria.model.DestinatarioTipo;
import com.microservicio.mensajeria.model.TipoMensaje;
import lombok.*;

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
    private Long cursoId;
    private String destinatarioNombre;
    private String destinatarioRol;

    private TipoMensaje tipoMensaje;
    private DestinatarioTipo destinatarioTipo;

}