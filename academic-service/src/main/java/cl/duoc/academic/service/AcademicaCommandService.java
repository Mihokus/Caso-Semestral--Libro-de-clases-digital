package cl.duoc.academic.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.duoc.academic.model.*;
import cl.duoc.academic.repository.*;
import cl.duoc.academic.dto.*;
@Service
public class AcademicaCommandService {
    @Autowired private EvaluacionRepository evalRepo;
    @Autowired private AsignaturaRepository asigRepo;
    @Autowired private CursoRepository cursoRepo;

    public Evaluacion registrarNota (EvaluacionDTO dto){
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

        return evalRepo.save(nueva);
    }

    public Curso guardarCurso(Curso curso){
        return cursoRepo.save(curso);
    }

 

}
