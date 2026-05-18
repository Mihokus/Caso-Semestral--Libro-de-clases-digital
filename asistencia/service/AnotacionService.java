package cl.colegio.ohiggins.asistencia.service;

import cl.colegio.ohiggins.asistencia.dto.AnotacionDTO;
import cl.colegio.ohiggins.asistencia.dto.AnotacionRequest;
import cl.colegio.ohiggins.asistencia.entity.Alumno;
import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.repository.AlumnoRepository;
import cl.colegio.ohiggins.asistencia.repository.AnotacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AnotacionService {

    @Autowired private AnotacionRepository anotacionRepo;
    @Autowired private AlumnoRepository alumnoRepo;
    @Autowired private AsistenciaMapper mapper;

    public AnotacionDTO registrar(AnotacionRequest req) {
        Anotacion an = new Anotacion();
        an.setAlumnoId(req.getAlumnoId());
        Alumno al = alumnoRepo.findById(req.getAlumnoId()).orElse(null);
        an.setAlumnoNombre(al != null ? al.getNombre() : null);
        an.setTipo(req.getTipo());
        an.setDescripcion(req.getDescripcion());
        an.setFecha(Instant.now());
        an.setRegistradoPorId(req.getRegistradoPorId());
        an.setRegistradoPorNombre(req.getRegistradoPorNombre());
        return mapper.toAnotacionDTO(anotacionRepo.save(an));
    }

    public List<AnotacionDTO> anotacionesAlumno(Long alumnoId) {
        return anotacionRepo.findByAlumnoIdOrderByFechaDesc(alumnoId)
                .stream().map(mapper::toAnotacionDTO).toList();
    }
}
