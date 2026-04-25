package cl.duoc.academic.facade;
import cl.duoc.academic.model.Evaluacion;
public interface AcademicFacade {
    Evaluacion registrarNota(Long asignaturaId, String nombre, Double nota);
    Double obtenerPromedioRendimiento(Long asignaturaId);
}