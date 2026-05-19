package cl.colegio.ohiggins.asistencia.service;

import cl.colegio.ohiggins.asistencia.dto.AlumnoDTO;
import cl.colegio.ohiggins.asistencia.dto.ApoderadoInfo;
import cl.colegio.ohiggins.asistencia.entity.Alumno;
import cl.colegio.ohiggins.asistencia.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlumnoService {

    @Autowired private AlumnoRepository alumnoRepo;
    @Autowired private AsistenciaMapper mapper;

    public List<AlumnoDTO> listarAlumnos() {
        return alumnoRepo.findAll().stream().map(mapper::toAlumnoDTO).toList();
    }

    public AlumnoDTO obtenerPorId(Long id) {
        return alumnoRepo.findById(id).map(mapper::toAlumnoDTO).orElse(null);
    }

    public List<ApoderadoInfo> obtenerApoderadosDe(Long alumnoId) {
        return alumnoRepo.findById(alumnoId)
                .map(a -> mapper.toApoderadoInfoList(a.getApoderados()))
                .orElse(new ArrayList<>());
    }

    public List<AlumnoDTO> listarPorCurso(Long cursoId) {
        return alumnoRepo.findByCursoId(cursoId).stream().map(mapper::toAlumnoDTO).toList();
    }

    public Alumno obtenerEntidadPorId(Long id) {
        return alumnoRepo.findById(id).orElse(null);
    }
}
