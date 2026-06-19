package cl.colegio.ohiggins.asistencia.strategy.anotacion;

import cl.colegio.ohiggins.asistencia.dto.AnotacionDTO;

public interface AnotacionStrategy {

    AnotacionDTO ejecutar(AnotacionContext context);
}