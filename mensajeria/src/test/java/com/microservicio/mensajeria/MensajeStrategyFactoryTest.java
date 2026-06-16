package com.microservicio.mensajeria;

import com.microservicio.mensajeria.model.TipoMensaje;
import com.microservicio.mensajeria.strategy.ComunicadoGeneralStrategy;
import com.microservicio.mensajeria.strategy.MensajeDirectoStrategy;
import com.microservicio.mensajeria.strategy.MensajeStrategy;
import com.microservicio.mensajeria.strategy.MensajeStrategyFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MensajeStrategyFactoryTest {

    @Test
    void deberiaRetornarStrategyDirecto() {
        MensajeStrategyFactory factory = new MensajeStrategyFactory(
                List.of(new MensajeDirectoStrategy(), new ComunicadoGeneralStrategy())
        );

        MensajeStrategy strategy = factory.obtenerStrategy(TipoMensaje.DIRECTO);

        assertNotNull(strategy);
        assertEquals(TipoMensaje.DIRECTO, strategy.getTipoMensaje());
        assertInstanceOf(MensajeDirectoStrategy.class, strategy);
    }

    @Test
    void deberiaRetornarStrategyComunicado() {
        MensajeStrategyFactory factory = new MensajeStrategyFactory(
                List.of(new MensajeDirectoStrategy(), new ComunicadoGeneralStrategy())
        );

        MensajeStrategy strategy = factory.obtenerStrategy(TipoMensaje.COMUNICADO);

        assertNotNull(strategy);
        assertEquals(TipoMensaje.COMUNICADO, strategy.getTipoMensaje());
        assertInstanceOf(ComunicadoGeneralStrategy.class, strategy);
    }

    @Test
    void deberiaFallarSiNoExisteStrategyParaTipoMensaje() {
        MensajeStrategyFactory factory = new MensajeStrategyFactory(
                List.of(new MensajeDirectoStrategy())
        );

        assertThrows(IllegalArgumentException.class,
                () -> factory.obtenerStrategy(TipoMensaje.COMUNICADO));
    }
}