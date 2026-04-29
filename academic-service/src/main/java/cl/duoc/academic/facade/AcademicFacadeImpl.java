package cl.duoc.academic.facade;
import cl.duoc.academic.model.*;
import cl.duoc.academic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class AcademicFacadeImpl implements AcademicFacade{
    @Autowired private AsignaturaRepository asigRepo;
    @Autowired private EvaluacionRepository evalRepo;

    @Override
    public Evaluacion registrarNota(Long asignaturaId,Long alumnoId,String nombre, Double nota, Double ponderacion){
        Asignatura asig = asigRepo.findById(asignaturaId)
        .orElseThrow(() -> new RuntimeException("Asignatura no encontrada"));

        Evaluacion nueva = new Evaluacion();
        nueva.setAsignatura(asig);
        nueva.setAlumnoId(alumnoId);
        nueva.setNombre(nombre);
        nueva.setNota(nota);
        nueva.setPonderacion(ponderacion);
        
        

        return evalRepo.save(nueva);
    }

    @Override
    public Double obtenerPromedioRendimiento (Long asignaturaId){
        List<Evaluacion> notas = evalRepo.findByAsignaturaId(asignaturaId);

        if(notas.isEmpty()) 
            return 0.0;

        return notas.stream().mapToDouble(Evaluacion::getNota).average().orElse(0.0);
    }
    
    @Override
    public Asignatura guardarAsignatura(Asignatura asignatura) {
        return asigRepo.save(asignatura);
    }

    @Override
    public List<Asignatura> listarAsignaturas() {
        return asigRepo.findAll();
    }

}