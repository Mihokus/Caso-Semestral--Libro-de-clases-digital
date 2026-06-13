package com.microservicio.mensajeria.strategy;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.model.TipoMensaje;

public interface MensajeStrategy {

    TipoMensaje getTipoMensaje();

    void validar(MensajeRequest request);

    void preparar(MensajeRequest request);
}