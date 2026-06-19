package cl.duoc.academic.facade;
import cl.duoc.academic.dto.AsignaturaDTO;
import cl.duoc.academic.dto.AsignaturaRequest;
import cl.duoc.academic.dto.CursoRequest;
import cl.duoc.academic.dto.CursoDTO;
import cl.duoc.academic.dto.EvaluacionDTO;
import cl.duoc.academic.dto.EvaluacionResponse;
import cl.duoc.academic.dto.RendimientoDTO;
import cl.duoc.academic.service.AcademicaCommandService;
import cl.duoc.academic.service.AcademicaQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AcademicFacadeImplTest {

    @Mock private AcademicaCommandService commandService;
    @Mock private AcademicaQueryService queryService;

    @InjectMocks private AcademicFacadeImpl facade;

    @Test
    public void registrarNota_delegaEnElServicioDeComandos() {
        EvaluacionDTO dto = new EvaluacionDTO();
        EvaluacionResponse esperado = new EvaluacionResponse();
        esperado.setId(1L);

        when(commandService.registrarNota(dto)).thenReturn(esperado);

        EvaluacionResponse resp = facade.registrarNota(dto);

        assertSame(esperado, resp);
        verify(commandService).registrarNota(dto);
    }

    @Test
    public void obtenerRendimientoTotal_delegaEnElServicioDeConsultas() {
        RendimientoDTO esperado = new RendimientoDTO(
                1L, "Matemáticas", 5.5, 4, new ArrayList<>());

        when(queryService.obtenerRendimientoRico(1L)).thenReturn(esperado);

        RendimientoDTO resp = facade.obtenerRendimientoTotal(1L);

        assertEquals("Matemáticas", resp.getAsignaturaNombre());
        verify(queryService).obtenerRendimientoRico(1L);
    }

    @Test
    public void listarCursos_delegaEnElServicioDeConsultas() {
        List<CursoDTO> esperado = List.of(
                new CursoDTO(1L, "1º Medio A", "Educación Media", 0));

        when(queryService.listarCursos()).thenReturn(esperado);

        assertEquals(1, facade.listarCursos().size());
        verify(queryService).listarCursos();
    }
    @Test
    public void guardarCurso_delegaEnElServicioDeComandos() {
        CursoRequest req = new CursoRequest();
        CursoDTO esperado = new CursoDTO(1L, "1º Medio A", "Educación Media", 0);

        when(commandService.guardarCurso(req)).thenReturn(esperado);

        assertSame(esperado, facade.guardarCurso(req));
        verify(commandService).guardarCurso(req);
    }

    @Test
    public void guardarAsignatura_delegaEnElServicioDeComandos() {
        AsignaturaRequest req = new AsignaturaRequest();
        AsignaturaDTO esperado = new AsignaturaDTO(1L, "Historia", null, null, null, null);

        when(commandService.guardarAsignatura(req)).thenReturn(esperado);

        assertSame(esperado, facade.guardarAsignatura(req));
        verify(commandService).guardarAsignatura(req);
    }

    @Test
    public void listarAsignaturas_delegaEnElServicioDeConsultas() {
        List<AsignaturaDTO> esperado = List.of(
                new AsignaturaDTO(1L, "Historia", null, null, null, null));

        when(queryService.listarAsignaturas()).thenReturn(esperado);

        assertEquals(1, facade.listarAsignaturas().size());
        verify(queryService).listarAsignaturas();
    }

    @Test
    public void obtenerAsignaturaPorId_delegaEnElServicioDeConsultas() {
        AsignaturaDTO esperado = new AsignaturaDTO(1L, "Historia", null, null, null, null);

        when(queryService.obtenerAsignaturaPorId(1L)).thenReturn(esperado);

        assertSame(esperado, facade.obtenerAsignaturaPorId(1L));
        verify(queryService).obtenerAsignaturaPorId(1L);
    }

    @Test
    public void obtenerNotasAlumno_delegaEnElServicioDeConsultas() {
        List<EvaluacionResponse> esperado = List.of(new EvaluacionResponse());

        when(queryService.obtenerNotasAlumno(10L)).thenReturn(esperado);

        assertEquals(1, facade.obtenerNotasAlumno(10L).size());
        verify(queryService).obtenerNotasAlumno(10L);
    }

    @Test
    public void obtenerCursoPorId_delegaEnElServicioDeConsultas() {
        CursoDTO esperado = new CursoDTO(1L, "1º Medio A", "Educación Media", 0);

        when(queryService.obtenerCursoPorId(1L)).thenReturn(esperado);

        assertSame(esperado, facade.obtenerCursoPorId(1L));
        verify(queryService).obtenerCursoPorId(1L);
    }
}