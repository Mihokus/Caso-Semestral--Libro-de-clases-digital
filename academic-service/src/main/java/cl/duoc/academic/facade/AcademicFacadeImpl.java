package cl.duoc.academic.facade;

import cl.duoc.academic.dto.AsignaturaDTO;
import cl.duoc.academic.dto.AsignaturaRequest;
import cl.duoc.academic.dto.CursoDTO;
import cl.duoc.academic.dto.CursoRequest;
import cl.duoc.academic.dto.EvaluacionDTO;
import cl.duoc.academic.dto.EvaluacionResponse;
import cl.duoc.academic.dto.RendimientoDTO;
import cl.duoc.academic.service.AcademicaCommandService;
import cl.duoc.academic.service.AcademicaQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AcademicFacadeImpl implements AcademicFacade {

    @Autowired private AcademicaCommandService commandService;
    @Autowired private AcademicaQueryService queryService;

    @Override
    public EvaluacionResponse registrarNota(EvaluacionDTO dto) {
        return commandService.registrarNota(dto);
    }

    @Override
    public CursoDTO guardarCurso(CursoRequest req) {
        return commandService.guardarCurso(req);
    }

    @Override
    public AsignaturaDTO guardarAsignatura(AsignaturaRequest req) {
        return commandService.guardarAsignatura(req);
    }

    @Override
    public List<AsignaturaDTO> listarAsignaturas() {
        return queryService.listarAsignaturas();
    }

    @Override
    public AsignaturaDTO obtenerAsignaturaPorId(Long id) {
        return queryService.obtenerAsignaturaPorId(id);
    }

    @Override
    public List<EvaluacionResponse> obtenerNotasAlumno(Long alumnoId) {
        return queryService.obtenerNotasAlumno(alumnoId);
    }

    @Override
    public RendimientoDTO obtenerRendimientoTotal(Long asignaturaId) {
        return queryService.obtenerRendimientoRico(asignaturaId);
    }

    @Override
    public List<CursoDTO> listarCursos() {
        return queryService.listarCursos();
    }

    @Override
    public CursoDTO obtenerCursoPorId(Long id) {
        return queryService.obtenerCursoPorId(id);
    }
}
