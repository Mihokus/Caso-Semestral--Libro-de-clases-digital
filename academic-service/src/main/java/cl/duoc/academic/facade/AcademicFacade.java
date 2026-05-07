package cl.duoc.academic.facade;
import cl.duoc.academic.model.*;
import cl.duoc.academic.dto.*;
import java.util.List;

public interface AcademicFacade {
    Evaluacion registrarNota(EvaluacionDTO dto);
    Curso guardarCurso (Curso curso);
    List<Evaluacion> obtenerNotasAlumno (Long alumnoId);
    RendimientoDTO obtenerRendimientoRico(Long asignaturaId);
    List<Curso> listarCursos();
}