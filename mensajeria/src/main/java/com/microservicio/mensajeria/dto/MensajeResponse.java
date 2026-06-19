package com.microservicio.mensajeria.dto;

import com.microservicio.mensajeria.model.TipoMensaje;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeResponse {
    private Long id;
    private TipoMensaje tipo;
    private String titulo;
    private String contenido;
    private UsuarioResumenDTO remitente;
    private UsuarioResumenDTO destinatario;
    private Long cursoId;
    private Instant fechaEnvio;
    private boolean leido;
}
