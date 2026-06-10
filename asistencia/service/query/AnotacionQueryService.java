package cl.colegio.ohiggins.asistencia.service.query;

import cl.colegio.ohiggins.asistencia.dto.AnotacionDTO;
import cl.colegio.ohiggins.asistencia.repository.AnotacionRepository;
import cl.colegio.ohiggins.asistencia.service.AsistenciaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnotacionQueryService {

    @Autowired
    private AnotacionRepository anotacionRepo;

    @Autowired
    private AsistenciaMapper mapper;

    /**
     * Obtiene historial de anotaciones de un alumno
     */
    public List<AnotacionDTO> anotacionesAlumno(Long alumnoId) {
        return anotacionRepo.findByAlumnoIdOrderByFechaDesc(alumnoId)
                .stream()
                .map(mapper::toAnotacionDTO)
                .toList();
    }
}