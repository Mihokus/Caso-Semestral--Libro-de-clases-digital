package com.microservicio.mensajeria.strategy;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.model.TipoMensaje;
import org.springframework.stereotype.Component;

@Component
public class ComunicadoGeneralStrategy implements MensajeStrategy {

    @Override
    public TipoMensaje getTipoMensaje() {
        return TipoMensaje.COMUNICADO;
    }

    @Override
    public void validar(MensajeRequest request) {
        if (request.getCursoId() == null) {
            throw new IllegalArgumentException("El comunicado requiere cursoId");
        }
    }

    @Override
    public void preparar(MensajeRequest request) {
        request.setDestinatarioId(null);
        request.setDestinatarioNombre(null);
        request.setDestinatarioRol(null);
    }
}