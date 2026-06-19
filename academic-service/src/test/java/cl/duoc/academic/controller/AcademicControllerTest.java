package cl.duoc.academic.controller;

import cl.duoc.academic.dto.AsignaturaDTO;
import cl.duoc.academic.dto.CursoDTO;
import cl.duoc.academic.dto.EvaluacionDTO;
import cl.duoc.academic.dto.EvaluacionResponse;
import cl.duoc.academic.facade.AcademicFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AcademicControllerTest {

    @Mock private AcademicFacade academicFacade;

    @InjectMocks private AcademicController controller;

    @Test
    public void registrarNota_devuelve200ConLaNota() {
        EvaluacionDTO dto = new EvaluacionDTO();
        EvaluacionResponse creada = new EvaluacionResponse();
        creada.setId(1L);
        creada.setNota(6.2);

        when(academicFacade.registrarNota(dto)).thenReturn(creada);

        ResponseEntity<EvaluacionResponse> resp = controller.registrarNota(dto);

        assertEquals(200, resp.getStatusCode().value());
        assertEquals(6.2, resp.getBody().getNota());
    }

    @Test
    public void listarNotasAlumno_devuelve200ConLaLista() {
        EvaluacionResponse nota = new EvaluacionResponse();
        nota.setId(1L);
        when(academicFacade.obtenerNotasAlumno(10L)).thenReturn(List.of(nota));

        ResponseEntity<List<EvaluacionResponse>> resp = controller.listarNotasAlumno(10L);

        assertEquals(200, resp.getStatusCode().value());
        assertEquals(1, resp.getBody().size());
    }

    @Test
    public void obtenerRendimiento_devuelve200() {
        cl.duoc.academic.dto.RendimientoDTO rendimiento =
                new cl.duoc.academic.dto.RendimientoDTO(1L, "Matemáticas", 5.5, 4, new java.util.ArrayList<>());

        when(academicFacade.obtenerRendimientoTotal(1L)).thenReturn(rendimiento);

        ResponseEntity<cl.duoc.academic.dto.RendimientoDTO> resp = controller.obtenerRendimiento(1L);

        assertEquals(200, resp.getStatusCode().value());
        assertEquals("Matemáticas", resp.getBody().getAsignaturaNombre());
    }

    @Test
    public void crearAsignatura_devuelve200() {
        cl.duoc.academic.dto.AsignaturaRequest req = new cl.duoc.academic.dto.AsignaturaRequest();
        AsignaturaDTO creada = new AsignaturaDTO(1L, "Historia", null, null, null, null);

        when(academicFacade.guardarAsignatura(req)).thenReturn(creada);

        ResponseEntity<AsignaturaDTO> resp = controller.crearAsignatura(req);

        assertEquals(200, resp.getStatusCode().value());
        assertEquals("Historia", resp.getBody().getNombre());
    }

    @Test
    public void listarAsignaturas_devuelve200ConLaLista() {
        when(academicFacade.listarAsignaturas()).thenReturn(
                List.of(new AsignaturaDTO(1L, "Historia", null, null, null, null)));

        ResponseEntity<List<AsignaturaDTO>> resp = controller.listarAsignaturas();

        assertEquals(200, resp.getStatusCode().value());
        assertEquals(1, resp.getBody().size());
    }

    @Test
    public void obtenerAsignaturaPorId_devuelve404SiNoExiste() {
        when(academicFacade.obtenerAsignaturaPorId(99L)).thenReturn(null);

        ResponseEntity<AsignaturaDTO> resp = controller.obtenerAsignaturaPorId(99L);

        assertEquals(404, resp.getStatusCode().value());
    }

    @Test
    public void obtenerAsignaturaPorId_devuelve200SiExiste() {
        AsignaturaDTO dto = new AsignaturaDTO(1L, "Historia", null, null, null, null);
        when(academicFacade.obtenerAsignaturaPorId(1L)).thenReturn(dto);

        ResponseEntity<AsignaturaDTO> resp = controller.obtenerAsignaturaPorId(1L);

        assertEquals(200, resp.getStatusCode().value());
        assertEquals("Historia", resp.getBody().getNombre());
    }

    @Test
    public void crearCurso_devuelve200() {
        cl.duoc.academic.dto.CursoRequest req = new cl.duoc.academic.dto.CursoRequest();
        CursoDTO creado = new CursoDTO(1L, "1º Medio A", "Educación Media", 0);

        when(academicFacade.guardarCurso(req)).thenReturn(creado);

        ResponseEntity<CursoDTO> resp = controller.crearCurso(req);

        assertEquals(200, resp.getStatusCode().value());
        assertEquals("1º Medio A", resp.getBody().getNombre());
    }

    @Test
    public void listarCursos_devuelve200ConLosCursos() {
        when(academicFacade.listarCursos()).thenReturn(
                List.of(new CursoDTO(1L, "1º Medio A", "Educación Media", 0)));

        ResponseEntity<List<CursoDTO>> resp = controller.listarCursos();

        assertEquals(200, resp.getStatusCode().value());
        assertEquals("1º Medio A", resp.getBody().get(0).getNombre());
    }

    @Test
    public void obtenerCursoPorId_devuelve404SiNoExiste() {
        when(academicFacade.obtenerCursoPorId(99L)).thenReturn(null);

        ResponseEntity<CursoDTO> resp = controller.obtenerCursoPorId(99L);

        assertEquals(404, resp.getStatusCode().value());
    }

    @Test
    public void obtenerCursoPorId_devuelve200SiExiste() {
        CursoDTO dto = new CursoDTO(1L, "1º Medio A", "Educación Media", 0);
        when(academicFacade.obtenerCursoPorId(1L)).thenReturn(dto);

        ResponseEntity<CursoDTO> resp = controller.obtenerCursoPorId(1L);

        assertEquals(200, resp.getStatusCode().value());
    }
}