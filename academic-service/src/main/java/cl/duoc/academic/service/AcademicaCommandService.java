package cl.duoc.academic.service;

import cl.duoc.academic.dto.AsignaturaDTO;
import cl.duoc.academic.dto.AsignaturaRequest;
import cl.duoc.academic.dto.CursoDTO;
import cl.duoc.academic.dto.CursoRequest;
import cl.duoc.academic.dto.EvaluacionDTO;
import cl.duoc.academic.dto.EvaluacionResponse;
import cl.duoc.academic.model.Asignatura;
import cl.duoc.academic.model.Curso;
import cl.duoc.academic.model.Evaluacion;
import cl.duoc.academic.repository.AsignaturaRepository;
import cl.duoc.academic.repository.CursoRepository;
import cl.duoc.academic.repository.EvaluacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcademicaCommandService {
    @Autowired private EvaluacionRepository evalRepo;
    @Autowired private AsignaturaRepository asigRepo;
    @Autowired private CursoRepository cursoRepo;
    @Autowired private AcademicaQueryService queryService;

    public EvaluacionResponse registrarNota(EvaluacionDTO dto) {
        Asignatura asig = asigRepo.findById(dto.getAsignaturaId())
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada"));

        Evaluacion nueva = new Evaluacion();
        nueva.setAsignatura(asig);
        nueva.setAsignaturaNombre(asig.getNombreAsignatura());
        nueva.setAlumnoId(dto.getAlumnoId());
        nueva.setAlumnoNombre(dto.getAlumnoNombre());
        nueva.setNombre(dto.getNombre());
        nueva.setNota(dto.getNota());
        nueva.setPonderacion(dto.getPonderacion());
        nueva.setRegistradoPorId(dto.getRegistradoPorId());
        nueva.setRegistradoPorNombre(dto.getRegistradoPorNombre());

        Evaluacion saved = evalRepo.save(nueva);
        return queryService.toEvaluacionResponse(saved);
    }

    public CursoDTO guardarCurso(CursoRequest req) {
        Curso c = new Curso();
        c.setNombre(req.getNombre());
        c.setNivel(req.getNivel());
        Curso saved = cursoRepo.save(c);
        return queryService.toCursoDTO(saved);
    }

    public AsignaturaDTO guardarAsignatura(AsignaturaRequest req) {
        Asignatura a = new Asignatura();
        a.setNombreAsignatura(req.getNombre());
        a.setDocenteId(req.getDocenteId());
        a.setDocenteNombre(req.getDocenteNombre());
        if (req.getCursoId() != null) {
            Curso curso = cursoRepo.findById(req.getCursoId())
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
            a.setCurso(curso);
        }
        Asignatura saved = asigRepo.save(a);
        return queryService.toAsignaturaDTO(saved);
    }
}
