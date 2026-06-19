package com.microservicio.mensajeria.strategy;

import com.microservicio.mensajeria.model.TipoMensaje;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class MensajeStrategyFactory {

    private final Map<TipoMensaje, MensajeStrategy> strategies = new EnumMap<>(TipoMensaje.class);

    public MensajeStrategyFactory(List<MensajeStrategy> strategyList) {
        for (MensajeStrategy strategy : strategyList) {
            strategies.put(strategy.getTipoMensaje(), strategy);
        }
    }

    public MensajeStrategy obtenerStrategy(TipoMensaje tipoMensaje) {
        MensajeStrategy strategy = strategies.get(tipoMensaje);

        if (strategy == null) {
            throw new IllegalArgumentException("No existe una estrategia para el tipo de mensaje: " + tipoMensaje);
        }

        return strategy;
    }
}