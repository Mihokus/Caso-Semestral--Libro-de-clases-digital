package com.libroclases.bff.dto;

import com.libroclases.bff.model.TipoMensaje;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajeDTO {
    private Long id;
    private TipoMensaje tipo;
    private String titulo;
    private String contenido;
    private PartyInfo remitente;
    private PartyInfo destinatario;
    private Long cursoId;
    private Instant fechaEnvio;
    private boolean leido;
}
