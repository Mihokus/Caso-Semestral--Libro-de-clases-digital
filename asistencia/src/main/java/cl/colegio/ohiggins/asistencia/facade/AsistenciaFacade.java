package cl.colegio.ohiggins.asistencia.facade;

import cl.colegio.ohiggins.asistencia.dto.AlumnoDTO;
import cl.colegio.ohiggins.asistencia.dto.AlumnoRequest;
import cl.colegio.ohiggins.asistencia.dto.AnotacionDTO;
import cl.colegio.ohiggins.asistencia.dto.AnotacionRequest;
import cl.colegio.ohiggins.asistencia.dto.ApoderadoInfo;
import cl.colegio.ohiggins.asistencia.dto.AsistenciaBulkRequest;
import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.model.EstadoAsistencia;
import cl.colegio.ohiggins.asistencia.model.TipoAnotacion;
import cl.colegio.ohiggins.asistencia.service.AsistenciaCommandService;
import cl.colegio.ohiggins.asistencia.service.AsistenciaQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class AsistenciaFacade {

    @Autowired private AsistenciaCommandService commandService;
    @Autowired private AsistenciaQueryService queryService;

    public List<AlumnoDTO> listarAlumnos() {
        return queryService.listarAlumnos();
    }

    public AlumnoDTO obtenerAlumno(Long id) {
        return queryService.obtenerAlumno(id);
    }

    public AlumnoDTO crearAlumno(AlumnoRequest req) {
        return commandService.crearAlumno(req);
    }

    public List<ApoderadoInfo> apoderadosDe(Long alumnoId) {
        return queryService.apoderadosDe(alumnoId);
    }

    public List<AlumnoDTO> alumnosDeCurso(Long cursoId) {
        return queryService.alumnosDeCurso(cursoId);
    }

    public List<AsistenciaDTO> historialAlumno(Long alumnoId) {
        return queryService.historialAlumno(alumnoId);
    }

    public List<AsistenciaDTO> porCursoYFecha(Long cursoId, LocalDate fecha) {
        return queryService.porCursoYFecha(cursoId, fecha);
    }

    /**
     * CASO DE USO 1: Registrar asistencia en lote Y generar alertas automáticas.
     * Acción 1: Registra la lista de asistencias del curso.
     * Acción 2: Si un alumno queda AUSENTE, genera automáticamente una anotación NEUTRAL de registro.
     */
    @Transactional
    public List<AsistenciaDTO> registrarAsistenciaBulk(AsistenciaBulkRequest req) {
        List<AsistenciaDTO> asistencias = commandService.registrarBulk(req);

        for (AsistenciaBulkRequest.Entrada entrada : req.getAsistencias()) {
            if (entrada.getEstado() == EstadoAsistencia.AUSENTE) {
                AnotacionRequest anotacionReq = new AnotacionRequest();
                anotacionReq.setAlumnoId(entrada.getAlumnoId());
                anotacionReq.setTipo(TipoAnotacion.NEUTRAL);
                anotacionReq.setDescripcion("Inasistencia automatica registrada el dia " + req.getFecha());
                anotacionReq.setRegistradoPorId(req.getRegistradoPorId());
                anotacionReq.setRegistradoPorNombre(req.getRegistradoPorNombre());

                commandService.registrarAnotacion(anotacionReq);
            }
        }
        return asistencias;
    }

    /**
     * CASO DE USO 2: Registrar una anotación condicional.
     * Acción 1: Registra la anotación solicitada (ej. Negativa).
     * Acción 2: Si el alumno ya acumula más de 3 anotaciones negativas en su historial,
     * se genera automáticamente una anotación NEUTRAL adicional citando al apoderado.
     */
    @Transactional
    public AnotacionDTO registrarAnotacion(AnotacionRequest req) {
        AnotacionDTO anotacionCreada = commandService.registrarAnotacion(req);

        if (req.getTipo() == TipoAnotacion.NEGATIVA) {
            List<AnotacionDTO> historial = queryService.anotacionesAlumno(req.getAlumnoId());
            long negativas = historial.stream().filter(a -> a.getTipo() == TipoAnotacion.NEGATIVA).count();

            if (negativas >= 3) {
                AnotacionRequest citacionReq = new AnotacionRequest();
                citacionReq.setAlumnoId(req.getAlumnoId());
                citacionReq.setTipo(TipoAnotacion.NEUTRAL);
                citacionReq.setDescripcion("ALERTA: El alumno acumula " + negativas + " anotaciones negativas. Se requiere citacion a apoderado.");
                citacionReq.setRegistradoPorId(req.getRegistradoPorId());
                citacionReq.setRegistradoPorNombre("Sistema Automatico");

                commandService.registrarAnotacion(citacionReq);
            }
        }
        return anotacionCreada;
    }

    public List<AnotacionDTO> anotacionesAlumno(Long alumnoId) {
        return queryService.anotacionesAlumno(alumnoId);
    }
}
