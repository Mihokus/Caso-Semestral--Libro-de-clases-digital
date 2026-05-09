package cl.duoc.academic.service;
import cl.duoc.academic.dto.RendimientoDTO;
import cl.duoc.academic.model.*;
import cl.duoc.academic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AcademicaQueryService {
    @Autowired private EvaluacionRepository evalRepo;
    @Autowired private CursoRepository cursoRepo;

    public List<Evaluacion> obtenerNotasAlumno(Long alumnoId){
        return evalRepo.findByAlumnoId(alumnoId);
    }

    public RendimientoDTO obtenerRendimientoRico(Long asignaturaId){
        List<Evaluacion> notas = evalRepo.findByAsignaturaId(asignaturaId);
        if (notas.isEmpty()) return new RendimientoDTO(0.0, 0, "Sin datos");

        Double promedio = notas.stream().mapToDouble(Evaluacion::getNota).average().orElse(0.0);
        String estado = promedio >= 4.0 ? "Aprobando" : "Riesgo Repitencia";

        return new RendimientoDTO(promedio, notas.size(), estado);
    }

    public List<Curso> listarCursos(){
        return cursoRepo.findAll();
    }
    
}
