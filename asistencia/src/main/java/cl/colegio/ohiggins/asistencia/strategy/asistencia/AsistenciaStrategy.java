package cl.colegio.ohiggins.asistencia.strategy.asistencia;

import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;

public interface AsistenciaStrategy {

    AsistenciaDTO ejecutar(AsistenciaContext context);
}