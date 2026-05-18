package cl.colegio.ohiggins.asistencia.service;

import cl.colegio.ohiggins.asistencia.dto.AsistenciaBulkRequest;
import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.entity.Alumno;
import cl.colegio.ohiggins.asistencia.model.Asistencia;
import cl.colegio.ohiggins.asistencia.repository.AlumnoRepository;
import cl.colegio.ohiggins.asistencia.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AsistenciaService {

    @Autowired private AsistenciaRepository asistenciaRepo;
    @Autowired private AlumnoRepository alumnoRepo;
    @Autowired private AsistenciaMapper mapper;

    public List<AsistenciaDTO> registrarBulk(AsistenciaBulkRequest req) {
        if (req == null || req.getAsistencias() == null || req.getAsistencias().isEmpty()) {
            return new ArrayList<>();
        }

        Map<Long, Alumno> alumnos = new HashMap<>();
        for (AsistenciaBulkRequest.Entrada e : req.getAsistencias()) {
            alumnoRepo.findById(e.getAlumnoId()).ifPresent(a -> alumnos.put(a.getId(), a));
        }

        List<AsistenciaDTO> out = new ArrayList<>();
        for (AsistenciaBulkRequest.Entrada e : req.getAsistencias()) {
            Alumno al = alumnos.get(e.getAlumnoId());
            Asistencia row = new Asistencia();
            row.setAlumnoId(e.getAlumnoId());
            row.setAlumnoNombre(al != null ? al.getNombre() : null);
            row.setCursoId(req.getCursoId());
            row.setCursoNombre(al != null ? al.getCursoNombre() : null);
            row.setFecha(req.getFecha());
            row.setEstado(e.getEstado());
            row.setRegistradoPorId(req.getRegistradoPorId());
            row.setRegistradoPorNombre(req.getRegistradoPorNombre());
            out.add(mapper.toAsistenciaDTO(asistenciaRepo.save(row)));
        }
        return out;
    }

    public List<AsistenciaDTO> historialAlumno(Long alumnoId) {
        return asistenciaRepo.findByAlumnoIdOrderByFechaDesc(alumnoId)
                .stream().map(mapper::toAsistenciaDTO).toList();
    }

    public List<AsistenciaDTO> porCursoYFecha(Long cursoId, LocalDate fecha) {
        return asistenciaRepo.findByCursoIdAndFecha(cursoId, fecha)
                .stream().map(mapper::toAsistenciaDTO).toList();
    }
}
