package cl.duoc.academic.facade;
import cl.duoc.academic.model.Evaluacion;
import cl.duoc.academic.model.Asignatura;
import java.util.List;
public interface AcademicFacade {
    Evaluacion registrarNota(Long asignaturaId, Long alumnoId, String nombre, Double nota, Double ponderacion);
    Double obtenerPromedioRendimiento(Long asignaturaId);
    Asignatura guardarAsignatura(Asignatura asignatura);
    List<Asignatura> listarAsignaturas();
}