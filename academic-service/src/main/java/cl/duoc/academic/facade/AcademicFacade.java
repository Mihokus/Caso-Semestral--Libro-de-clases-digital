package cl.duoc.academic.facade;
import cl.duoc.academic.model.*;
import cl.duoc.academic.dto.*;
import java.util.List;

public interface AcademicFacade {
    Evaluacion registrarNota(EvaluacionDTO dto);
    Curso guardarCurso (Curso curso);
    Asignatura guardarAsignatura(Asignatura asignatura);
    List<Asignatura> listarAsignaturas();
    List<Evaluacion> obtenerNotasAlumno (Long alumnoId);
    RendimientoDTO obtenerRendimientoTotal(Long asignaturaId);
    List<Curso> listarCursos();
}