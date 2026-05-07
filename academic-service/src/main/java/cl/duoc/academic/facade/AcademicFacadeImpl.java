package cl.duoc.academic.facade;
import cl.duoc.academic.dto.*;
import cl.duoc.academic.model.*;
import cl.duoc.academic.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
@Component
public class AcademicFacadeImpl implements AcademicFacade{

    @Autowired private AcademicaCommandService commandService;
    @Autowired private AcademicaQueryService queryService;

    @Override
    public Evaluacion registrarNota(EvaluacionDTO dto){
        return commandService.registrarNota(dto);
    }

    @Override
    public Curso guardarCurso(Curso curso){
        return commandService.guardarCurso(curso);
    }

    @Override
    public List<Evaluacion> obtenerNotasAlumno(Long alumnoId){
        return queryService.obtenerNotasAlumno(alumnoId);
    }

    @Override
    public RendimientoDTO obtenerRedimiento(Long asignaturaId){
        return queryService.obtenerRedimiento(asignaturaId);
    }

    @Override
    public List<Curso> listarCursos(){
        return queryService.listarCursos();
    }
    
}