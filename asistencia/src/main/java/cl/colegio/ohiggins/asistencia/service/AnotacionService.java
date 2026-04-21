package cl.colegio.ohiggins.asistencia.service;

import cl.colegio.ohiggins.asistencia.model.Anotacion;
import java.util.List;

public interface AnotacionService {
    Anotacion guardarAnotacion(Anotacion anotacion);
    List<Anotacion> obtenerPorEstudiante(String rut);
}