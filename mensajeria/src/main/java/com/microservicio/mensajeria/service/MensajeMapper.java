package com.microservicio.mensajeria.service;

import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.dto.UsuarioResumenDTO;
import com.microservicio.mensajeria.model.EstadoMensaje;
import com.microservicio.mensajeria.model.Mensaje;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class MensajeMapper {

    public MensajeResponse toResponse(Mensaje m) {
        UsuarioResumenDTO remitente = new UsuarioResumenDTO(
                m.getRemitenteId(), m.getRemitenteNombre(), m.getRemitenteRol());
        UsuarioResumenDTO destinatario = m.getDestinatarioId() == null
                ? null
                : new UsuarioResumenDTO(m.getDestinatarioId(), m.getDestinatarioNombre(), m.getDestinatarioRol());

        LocalDateTime fecha = m.getFechaEnvio();
        return new MensajeResponse(
                m.getId(),
                m.getTipoMensaje(),
                m.getTitulo(),
                m.getContenido(),
                remitente,
                destinatario,
                m.getCursoId(),
                fecha == null ? null : fecha.toInstant(ZoneOffset.UTC),
                m.getEstado() == EstadoMensaje.LEIDO);
    }
}
