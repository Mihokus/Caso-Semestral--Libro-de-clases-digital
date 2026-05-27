package cl.duoc.academic.facade;

import cl.duoc.academic.dto.AsignaturaDTO;
import cl.duoc.academic.dto.AsignaturaRequest;
import cl.duoc.academic.dto.CursoDTO;
import cl.duoc.academic.dto.CursoRequest;
import cl.duoc.academic.dto.EvaluacionDTO;
import cl.duoc.academic.dto.EvaluacionResponse;
import cl.duoc.academic.dto.RendimientoDTO;

import java.util.List;

public interface AcademicFacade {
    EvaluacionResponse registrarNota(EvaluacionDTO dto);
    CursoDTO guardarCurso(CursoRequest req);
    AsignaturaDTO guardarAsignatura(AsignaturaRequest req);
    List<AsignaturaDTO> listarAsignaturas();
    AsignaturaDTO obtenerAsignaturaPorId(Long id);
    List<EvaluacionResponse> obtenerNotasAlumno(Long alumnoId);
    RendimientoDTO obtenerRendimientoTotal(Long asignaturaId);
    List<CursoDTO> listarCursos();
    CursoDTO obtenerCursoPorId(Long id);
}
