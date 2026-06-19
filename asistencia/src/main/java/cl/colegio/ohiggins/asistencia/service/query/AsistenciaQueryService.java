package cl.colegio.ohiggins.asistencia.service.query;

import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.repository.AsistenciaRepository;
import cl.colegio.ohiggins.asistencia.service.AsistenciaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AsistenciaQueryService {

    @Autowired
    private AsistenciaRepository asistenciaRepo;

    @Autowired
    private AsistenciaMapper mapper;

    /**
     * Historial completo de asistencia de un alumno
     */
    public List<AsistenciaDTO> historialAlumno(Long alumnoId) {
        return asistenciaRepo.findByAlumnoIdOrderByFechaDesc(alumnoId)
                .stream()
                .map(mapper::toAsistenciaDTO)
                .toList();
    }

    /**
     * Asistencias por curso y fecha
     */
    public List<AsistenciaDTO> porCursoYFecha(Long cursoId, LocalDate fecha) {
        return asistenciaRepo.findByCursoIdAndFecha(cursoId, fecha)
                .stream()
                .map(mapper::toAsistenciaDTO)
                .toList();
    }
}