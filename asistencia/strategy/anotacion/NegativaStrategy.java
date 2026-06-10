package cl.colegio.ohiggins.asistencia.strategy.anotacion;

import cl.colegio.ohiggins.asistencia.dto.AnotacionDTO;
import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.model.TipoAnotacion;
import cl.colegio.ohiggins.asistencia.repository.AnotacionRepository;
import cl.colegio.ohiggins.asistencia.service.AsistenciaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class NegativaStrategy implements AnotacionStrategy {

    @Autowired
    private AnotacionRepository repo;

    @Autowired
    private AsistenciaMapper mapper;

    @Override
    public AnotacionDTO ejecutar(AnotacionContext context) {

        // 1. guardar anotación negativa
        Anotacion a = new Anotacion();
        a.setAlumnoId(context.getAlumnoId());
        a.setTipo(TipoAnotacion.NEGATIVA);
        a.setDescripcion(context.getRequest().getDescripcion());
        a.setFecha(Instant.now());
        a.setRegistradoPorId(context.getRequest().getRegistradoPorId());
        a.setRegistradoPorNombre(context.getRequest().getRegistradoPorNombre());

        Anotacion saved = repo.save(a);

        // 2. contar negativas
        long negativas = repo.findByAlumnoIdOrderByFechaDesc(context.getAlumnoId())
                .stream()
                .filter(x -> x.getTipo() == TipoAnotacion.NEGATIVA)
                .count();

        // 3. regla de negocio
        if (negativas >= 3) {

            Anotacion alerta = new Anotacion();
            alerta.setAlumnoId(context.getAlumnoId());
            alerta.setTipo(TipoAnotacion.NEUTRAL);
            alerta.setDescripcion("ALERTA: alumno acumula " + negativas + " anotaciones negativas. Requiere citación apoderado.");
            alerta.setFecha(Instant.now());
            alerta.setRegistradoPorId(context.getRequest().getRegistradoPorId());
            alerta.setRegistradoPorNombre("Sistema");

            repo.save(alerta);
        }

        return mapper.toAnotacionDTO(saved);
    }
}