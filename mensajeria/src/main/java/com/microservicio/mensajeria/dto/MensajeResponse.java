package com.microservicio.mensajeria.dto;

import com.microservicio.mensajeria.model.DestinatarioTipo;
import com.microservicio.mensajeria.model.EstadoMensaje;
import com.microservicio.mensajeria.model.TipoMensaje;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajeResponse {

    private Long id;

    private String titulo;
    private String contenido;

    private String remitenteNombre;
    private String destinatarioNombre;

    private TipoMensaje tipoMensaje;
    private DestinatarioTipo destinatarioTipo;

    private EstadoMensaje estado;
    private LocalDateTime fechaEnvio;

}