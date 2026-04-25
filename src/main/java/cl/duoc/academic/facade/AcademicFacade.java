package cl.duoc.academic.facade;
import cl.duoc.academic.model.Evaluacion;
import cl.duoc.academic.model.Asignatura;
import java.util.List;
public interface AcademicFacade {
    Evaluacion registrarNota(Long asignaturaId, String nombre, Double nota);
    Double obtenerPromedioRendimiento(Long asignaturaId);
    Asignatura guardarAsignatura(Asignatura asignatura);
    List<Asignatura> listarAsignaturas();
}