package cl.colegio.ohiggins.asistencia.service;

import cl.colegio.ohiggins.asistencia.model.Asistencia;
import java.util.List;

public interface AsistenciaService {
    Asistencia registrar(Asistencia asistencia);
    List<Asistencia> obtenerTodo();
}