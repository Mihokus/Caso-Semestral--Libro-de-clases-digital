package cl.duoc.academic.service;

import cl.duoc.academic.dto.EvaluacionResponse;
import cl.duoc.academic.model.Asignatura;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cl.duoc.academic.dto.AsignaturaRequest;
import cl.duoc.academic.dto.EvaluacionDTO;
import cl.duoc.academic.dto.AsignaturaRequest;
import cl.duoc.academic.model.Evaluacion;
import cl.duoc.academic.repository.AsignaturaRepository;
import cl.duoc.academic.repository.CursoRepository;
import cl.duoc.academic.repository.EvaluacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AcademicaCommandServiceTest {

    @Mock private EvaluacionRepository evalRepo;
    @Mock private AsignaturaRepository asigRepo;
    @Mock private CursoRepository cursoRepo;
    @Mock private AcademicaQueryService queryService;

    @InjectMocks private AcademicaCommandService commandService;

    @Test
    public void registrarNota_lanzaExcepcionSiLaAsignaturaNoExiste() {
        
        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setAsignaturaId(99L);

        when(asigRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commandService.registrarNota(dto));

        verify(evalRepo, never()).save(any(Evaluacion.class));
    }

    @Test
    public void registrarNota_guardaLaEvaluacionConLosDatosDelDto() {
        Asignatura asig = new Asignatura();
        asig.setId(1L);
        asig.setNombreAsignatura("Lenguaje");

        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setAsignaturaId(1L);
        dto.setAlumnoId(10L);
        dto.setAlumnoNombre("Ana Soto");
        dto.setNombre("Prueba 1");
        dto.setNota(6.5);
        dto.setPonderacion(30.0);

        when(asigRepo.findById(1L)).thenReturn(Optional.of(asig));
        when(evalRepo.save(any(Evaluacion.class))).thenAnswer(inv -> {
            Evaluacion e = inv.getArgument(0);
            e.setId(100L);
            return e;
        });
        when(queryService.toEvaluacionResponse(any(Evaluacion.class))).thenAnswer(inv -> {
            Evaluacion e = inv.getArgument(0);
            EvaluacionResponse r = new EvaluacionResponse();
            r.setId(e.getId());
            r.setAlumnoNombre(e.getAlumnoNombre());
            r.setNota(e.getNota());
            r.setAsignaturaNombre(e.getAsignaturaNombre());
            return r;
        });

        EvaluacionResponse resp = commandService.registrarNota(dto);
        assertEquals(100L, resp.getId());
        assertEquals("Ana Soto", resp.getAlumnoNombre());
        assertEquals(6.5, resp.getNota());
        assertEquals("Lenguaje", resp.getAsignaturaNombre());
        verify(evalRepo).save(any(Evaluacion.class));
    }
    @Test
    public void guardarAsignatura_lanzaExcepcionSiElCursoNoExiste() {
        AsignaturaRequest req = new AsignaturaRequest();
        req.setNombre("Física");
        req.setCursoId(50L);

        when(cursoRepo.findById(50L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commandService.guardarAsignatura(req));
        verify(asigRepo, never()).save(any(Asignatura.class));
    }
}