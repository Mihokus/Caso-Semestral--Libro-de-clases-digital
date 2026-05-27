package cl.colegio.ohiggins.asistencia.service;

import cl.colegio.ohiggins.asistencia.dto.AlumnoDTO;
import cl.colegio.ohiggins.asistencia.dto.AlumnoRequest;
import cl.colegio.ohiggins.asistencia.dto.AnotacionDTO;
import cl.colegio.ohiggins.asistencia.dto.AnotacionRequest;
import cl.colegio.ohiggins.asistencia.dto.AsistenciaBulkRequest;
import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.entity.Alumno;
import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.model.Asistencia;
import cl.colegio.ohiggins.asistencia.repository.AlumnoRepository;
import cl.colegio.ohiggins.asistencia.repository.AnotacionRepository;
import cl.colegio.ohiggins.asistencia.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AsistenciaCommandService {

    @Autowired private AsistenciaRepository asistenciaRepo;
    @Autowired private AnotacionRepository anotacionRepo;
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

    public AnotacionDTO registrarAnotacion(AnotacionRequest req) {
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

    public AlumnoDTO crearAlumno(AlumnoRequest req) {
        Alumno alumno = new Alumno();
        alumno.setNombre(req.getNombre());
        alumno.setRut(req.getRut());
        alumno.setCursoId(req.getCursoId());
        alumno.setCursoNombre(req.getCursoNombre());
        return mapper.toAlumnoDTO(alumnoRepo.save(alumno));
    }
}
