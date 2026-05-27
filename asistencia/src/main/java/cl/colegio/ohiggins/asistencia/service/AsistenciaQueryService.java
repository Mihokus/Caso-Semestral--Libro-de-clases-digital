package cl.colegio.ohiggins.asistencia.service;

import cl.colegio.ohiggins.asistencia.dto.AlumnoDTO;
import cl.colegio.ohiggins.asistencia.dto.AnotacionDTO;
import cl.colegio.ohiggins.asistencia.dto.ApoderadoInfo;
import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.repository.AlumnoRepository;
import cl.colegio.ohiggins.asistencia.repository.AnotacionRepository;
import cl.colegio.ohiggins.asistencia.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AsistenciaQueryService {

    @Autowired private AsistenciaRepository asistenciaRepo;
    @Autowired private AnotacionRepository anotacionRepo;
    @Autowired private AlumnoRepository alumnoRepo;
    @Autowired private AsistenciaMapper mapper;

    public List<AlumnoDTO> listarAlumnos() {
        return alumnoRepo.findAll().stream().map(mapper::toAlumnoDTO).toList();
    }

    public AlumnoDTO obtenerAlumno(Long id) {
        return alumnoRepo.findById(id).map(mapper::toAlumnoDTO).orElse(null);
    }

    public List<ApoderadoInfo> apoderadosDe(Long alumnoId) {
        return alumnoRepo.findById(alumnoId)
                .map(a -> mapper.toApoderadoInfoList(a.getApoderados()))
                .orElse(new ArrayList<>());
    }

    public List<AlumnoDTO> alumnosDeCurso(Long cursoId) {
        return alumnoRepo.findByCursoId(cursoId).stream().map(mapper::toAlumnoDTO).toList();
    }

    public List<AsistenciaDTO> historialAlumno(Long alumnoId) {
        return asistenciaRepo.findByAlumnoIdOrderByFechaDesc(alumnoId)
                .stream().map(mapper::toAsistenciaDTO).toList();
    }

    public List<AsistenciaDTO> porCursoYFecha(Long cursoId, LocalDate fecha) {
        return asistenciaRepo.findByCursoIdAndFecha(cursoId, fecha)
                .stream().map(mapper::toAsistenciaDTO).toList();
    }

    public List<AnotacionDTO> anotacionesAlumno(Long alumnoId) {
        return anotacionRepo.findByAlumnoIdOrderByFechaDesc(alumnoId)
                .stream().map(mapper::toAnotacionDTO).toList();
    }
}
