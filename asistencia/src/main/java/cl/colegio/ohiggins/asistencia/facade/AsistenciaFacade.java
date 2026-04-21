package cl.colegio.ohiggins.asistencia.facade;

import cl.colegio.ohiggins.asistencia.dto.AnotacionDTO;
import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.model.Asistencia;
import cl.colegio.ohiggins.asistencia.service.AsistenciaService;
import cl.colegio.ohiggins.asistencia.service.AnotacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AsistenciaFacade {

    @Autowired
    private AsistenciaService asistenciaService;

    @Autowired
    private AnotacionService anotacionService;

    // Antes: registrarAsistencia -> Ahora: ejecutarRegistro (como pide AsistenciaController)
    public Asistencia ejecutarRegistro(Asistencia asistencia) {
        return asistenciaService.registrar(asistencia);
    }

    // Antes: obtenerHistorialAsistencia -> Ahora: historial (como pide AsistenciaController)
    public List<Asistencia> historial() {
        return asistenciaService.obtenerTodo();
    }

    // Nuevo método: registrarEventoDiario (como pide AnotacionController)
    // Nota: El error decía que recibía (<nulltype>, Anotacion), ajustamos a recibir el modelo.
    public Anotacion registrarEventoDiario(Object session, Anotacion anotacion) {
        return anotacionService.guardarAnotacion(anotacion);
    }
}