package cl.colegio.ohiggins.asistencia.facade;

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

    public Asistencia ejecutarRegistro(Asistencia asistencia) {
        return asistenciaService.registrar(asistencia);
    }

    public List<Asistencia> historial() {
        return asistenciaService.obtenerTodo();
    }

    public Anotacion registrarEventoDiario(Anotacion anotacion) {
        return anotacionService.guardarAnotacion(anotacion);
    }
}