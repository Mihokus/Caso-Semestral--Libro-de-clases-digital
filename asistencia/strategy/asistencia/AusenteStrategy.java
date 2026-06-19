package cl.colegio.ohiggins.asistencia.strategy.asistencia;

import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.model.Asistencia;
import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.model.TipoAnotacion;
import cl.colegio.ohiggins.asistencia.repository.AsistenciaRepository;
import cl.colegio.ohiggins.asistencia.repository.AnotacionRepository;
import cl.colegio.ohiggins.asistencia.service.AsistenciaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AusenteStrategy implements AsistenciaStrategy {

    @Autowired
    private AsistenciaRepository asistenciaRepo;

    @Autowired
    private AnotacionRepository anotacionRepo;

    @Autowired
    private AsistenciaMapper mapper;

    @Override
    public AsistenciaDTO ejecutar(AsistenciaContext context) {

        // 1. guardar asistencia
        Asistencia a = new Asistencia();
        a.setAlumnoId(context.getAlumnoId());
        a.setCursoId(context.getCursoId());
        a.setFecha(context.getFecha());
        a.setEstado(context.getEstado());
        a.setRegistradoPorId(context.getRegistradoPorId());
        a.setRegistradoPorNombre(context.getRegistradoPorNombre());

        Asistencia saved = asistenciaRepo.save(a);

        // 2. anotación automática por inasistencia
        Anotacion an = new Anotacion();
        an.setAlumnoId(context.getAlumnoId());
        an.setAlumnoNombre(null);
        an.setTipo(TipoAnotacion.NEUTRAL);
        an.setDescripcion("Inasistencia registrada automáticamente");
        an.setFecha(Instant.now());
        an.setRegistradoPorId(context.getRegistradoPorId());
        an.setRegistradoPorNombre("Sistema");

        anotacionRepo.save(an);

        return mapper.toAsistenciaDTO(saved);
    }
}