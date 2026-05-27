package cl.duoc.academic.service;

import cl.duoc.academic.dto.AsignaturaDTO;
import cl.duoc.academic.dto.CursoDTO;
import cl.duoc.academic.dto.EvaluacionResponse;
import cl.duoc.academic.dto.RegistradoPor;
import cl.duoc.academic.dto.RendimientoDTO;
import cl.duoc.academic.model.Asignatura;
import cl.duoc.academic.model.Curso;
import cl.duoc.academic.model.Evaluacion;
import cl.duoc.academic.repository.AsignaturaRepository;
import cl.duoc.academic.repository.CursoRepository;
import cl.duoc.academic.repository.EvaluacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AcademicaQueryService {
    @Autowired private EvaluacionRepository evalRepo;
    @Autowired private CursoRepository cursoRepo;
    @Autowired private AsignaturaRepository asigRepo;

    public List<EvaluacionResponse> obtenerNotasAlumno(Long alumnoId) {
        List<EvaluacionResponse> out = new ArrayList<>();
        for (Evaluacion e : evalRepo.findByAlumnoId(alumnoId)) {
            out.add(toEvaluacionResponse(e));
        }
        return out;
    }

    public RendimientoDTO obtenerRendimientoRico(Long asignaturaId) {
        Asignatura asig = asigRepo.findById(asignaturaId).orElse(null);
        String asigNombre = asig != null ? asig.getNombreAsignatura() : null;

        List<Evaluacion> notas = evalRepo.findByAsignaturaId(asignaturaId);

        if (notas.isEmpty()) {
            return new RendimientoDTO(asignaturaId, asigNombre, 0.0, 0, new ArrayList<>());
        }

        double promedioCurso = notas.stream().mapToDouble(Evaluacion::getNota).average().orElse(0.0);

        Map<Long, List<Evaluacion>> porAlumno = new LinkedHashMap<>();
        for (Evaluacion e : notas) {
            porAlumno.computeIfAbsent(e.getAlumnoId(), k -> new ArrayList<>()).add(e);
        }

        List<RendimientoDTO.RendimientoAlumno> alumnos = new ArrayList<>();
        for (Map.Entry<Long, List<Evaluacion>> entry : porAlumno.entrySet()) {
            List<Evaluacion> evs = entry.getValue();
            double promAlumno = evs.stream().mapToDouble(Evaluacion::getNota).average().orElse(0.0);
            String nombre = evs.get(0).getAlumnoNombre();
            alumnos.add(new RendimientoDTO.RendimientoAlumno(entry.getKey(), nombre, promAlumno, evs.size()));
        }
        alumnos.sort(Comparator.comparing(RendimientoDTO.RendimientoAlumno::getAlumnoNombre,
                Comparator.nullsLast(String::compareTo)));

        return new RendimientoDTO(asignaturaId, asigNombre, promedioCurso, notas.size(), alumnos);
    }

    public List<CursoDTO> listarCursos() {
        List<CursoDTO> out = new ArrayList<>();
        for (Curso c : cursoRepo.findAll()) {
            out.add(toCursoDTO(c));
        }
        return out;
    }

    public CursoDTO obtenerCursoPorId(Long id) {
        return cursoRepo.findById(id).map(this::toCursoDTO).orElse(null);
    }

    public List<AsignaturaDTO> listarAsignaturas() {
        List<AsignaturaDTO> out = new ArrayList<>();
        for (Asignatura a : asigRepo.findAll()) {
            out.add(toAsignaturaDTO(a));
        }
        return out;
    }

    public AsignaturaDTO obtenerAsignaturaPorId(Long id) {
        return asigRepo.findById(id).map(this::toAsignaturaDTO).orElse(null);
    }

    public EvaluacionResponse toEvaluacionResponse(Evaluacion e) {
        Long asigId = e.getAsignatura() != null ? e.getAsignatura().getId() : null;
        RegistradoPor rp = new RegistradoPor(e.getRegistradoPorId(), e.getRegistradoPorNombre());
        return new EvaluacionResponse(
                e.getId(),
                e.getAlumnoId(),
                e.getAlumnoNombre(),
                asigId,
                e.getAsignaturaNombre(),
                e.getNombre(),
                e.getNota(),
                e.getPonderacion(),
                e.getFecha(),
                rp);
    }

    public AsignaturaDTO toAsignaturaDTO(Asignatura a) {
        Long cursoId = a.getCurso() != null ? a.getCurso().getId() : null;
        String cursoNombre = a.getCurso() != null ? a.getCurso().getNombre() : null;
        return new AsignaturaDTO(
                a.getId(),
                a.getNombreAsignatura(),
                cursoId,
                cursoNombre,
                a.getDocenteId(),
                a.getDocenteNombre());
    }

    public CursoDTO toCursoDTO(Curso c) {
        return new CursoDTO(c.getId(), c.getNombre(), c.getNivel(), 0);
    }
}
