package cl.colegio.ohiggins.asistencia.strategy.anotacion;

import cl.colegio.ohiggins.asistencia.dto.AnotacionDTO;
import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.repository.AnotacionRepository;
import cl.colegio.ohiggins.asistencia.service.AsistenciaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class NeutralStrategy implements AnotacionStrategy {

    @Autowired
    private AnotacionRepository repo;

    @Autowired
    private AsistenciaMapper mapper;

    @Override
    public AnotacionDTO ejecutar(AnotacionContext context) {

        Anotacion a = new Anotacion();
        a.setAlumnoId(context.getAlumnoId());
        a.setTipo(context.getRequest().getTipo());
        a.setDescripcion(context.getRequest().getDescripcion());
        a.setFecha(Instant.now());
        a.setRegistradoPorId(context.getRequest().getRegistradoPorId());
        a.setRegistradoPorNombre(context.getRequest().getRegistradoPorNombre());

        return mapper.toAnotacionDTO(repo.save(a));
    }
}