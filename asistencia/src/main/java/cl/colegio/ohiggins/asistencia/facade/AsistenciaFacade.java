package cl.colegio.ohiggins.asistencia.facade;

// BORRA los imports que digan .entity y asegúrate que digan .model:
import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.model.Asistencia;

// Los demás se mantienen igual
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

    /**
     * Registra una nueva asistencia usando el servicio.
     * Coincide con: facade.ejecutarRegistro(asistencia) en AsistenciaController
     */
    public Asistencia ejecutarRegistro(Asistencia asistencia) {
        return asistenciaService.registrar(asistencia);
    }

    /**
     * Obtiene todos los registros de asistencia.
     * Coincide con: facade.historial() en AsistenciaController
     */
    public List<Asistencia> historial() {
        return asistenciaService.obtenerTodo();
    }

    /**
     * Registra una anotación o evento diario.
     * Se eliminó el parámetro 'session' que causaba el error de nulltype.
     */
    public Anotacion registrarEventoDiario(Anotacion anotacion) {
        return anotacionService.guardarAnotacion(anotacion);
    }
}