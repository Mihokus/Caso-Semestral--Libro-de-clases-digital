package com.microservicio.mensajeria.strategy;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.model.TipoMensaje;
import org.springframework.stereotype.Component;

@Component
public class MensajeDirectoStrategy implements MensajeStrategy {

    @Override
    public TipoMensaje getTipoMensaje() {
        return TipoMensaje.DIRECTO;
    }

    @Override
    public void validar(MensajeRequest request) {
        if (request.getDestinatarioId() == null) {
            throw new IllegalArgumentException("El mensaje directo requiere destinatarioId");
        }

        if (request.getDestinatarioNombre() == null || request.getDestinatarioNombre().isBlank()) {
            throw new IllegalArgumentException("El mensaje directo requiere destinatarioNombre");
        }

        if (request.getDestinatarioRol() == null || request.getDestinatarioRol().isBlank()) {
            throw new IllegalArgumentException("El mensaje directo requiere destinatarioRol");
        }
    }

    @Override
    public void preparar(MensajeRequest request) {
    }
}