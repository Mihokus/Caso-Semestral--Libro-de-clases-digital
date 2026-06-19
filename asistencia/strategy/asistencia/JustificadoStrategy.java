package cl.colegio.ohiggins.asistencia.strategy.asistencia;

import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.model.Asistencia;
import cl.colegio.ohiggins.asistencia.repository.AsistenciaRepository;
import cl.colegio.ohiggins.asistencia.service.AsistenciaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JustificadoStrategy implements AsistenciaStrategy {

    @Autowired
    private AsistenciaRepository repo;

    @Autowired
    private AsistenciaMapper mapper;

    @Override
    public AsistenciaDTO ejecutar(AsistenciaContext context) {

        Asistencia a = new Asistencia();
        a.setAlumnoId(context.getAlumnoId());
        a.setCursoId(context.getCursoId());
        a.setFecha(context.getFecha());
        a.setEstado(context.getEstado());
        a.setRegistradoPorId(context.getRegistradoPorId());
        a.setRegistradoPorNombre(context.getRegistradoPorNombre());

        return mapper.toAsistenciaDTO(repo.save(a));
    }
}