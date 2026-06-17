package cl.duoc.academic.service;

import cl.duoc.academic.dto.CursoDTO;
import cl.duoc.academic.dto.EvaluacionResponse;
import cl.duoc.academic.dto.RendimientoDTO;
import cl.duoc.academic.model.Asignatura;
import cl.duoc.academic.model.Curso;
import cl.duoc.academic.model.Evaluacion;
import cl.duoc.academic.repository.AsignaturaRepository;
import cl.duoc.academic.repository.CursoRepository;
import cl.duoc.academic.repository.EvaluacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AcademicaQueryServiceTest {

    @Mock private EvaluacionRepository evalRepo;
    @Mock private CursoRepository cursoRepo;
    @Mock private AsignaturaRepository asigRepo;

    @InjectMocks private AcademicaQueryService queryService;

    @Test
    public void promedioCursoDePromedioDeNotasDelAsignatura() {
        Long asignaturaId = 1L;
        Asignatura asig = new Asignatura();
        asig.setId(asignaturaId);
        asig.setNombreAsignatura("Matemáticas");

        Evaluacion e1 = new Evaluacion();
        e1.setAlumnoId(10L);
        e1.setAlumnoNombre("Ana");
        e1.setNota(6.0);

        Evaluacion e2 = new Evaluacion();
        e2.setAlumnoId(11L);
        e2.setAlumnoNombre("Bruno");
        e2.setNota(4.0);

        when(asigRepo.findById(asignaturaId)).thenReturn(Optional.of(asig));
        when(evalRepo.findByAsignaturaId(asignaturaId)).thenReturn(Arrays.asList(e1, e2));

        RendimientoDTO rendimiento = queryService.obtenerRendimientoRico(asignaturaId);

        assertEquals(5.0, rendimiento.getPromedioCurso(), 0.0001);
        assertEquals(2, rendimiento.getCantidadEvaluaciones());
        assertEquals(2, rendimiento.getAlumnos().size());
        assertEquals("Matemáticas", rendimiento.getAsignaturaNombre());
    }

    @Test
    public void rendimientoVacioDevuelveListaAlumnosVaciaNoNull() {
        Long asignaturaId = 1L;
        when(asigRepo.findById(asignaturaId)).thenReturn(Optional.empty());
        when(evalRepo.findByAsignaturaId(asignaturaId)).thenReturn(Collections.emptyList());

        RendimientoDTO rendimiento = queryService.obtenerRendimientoRico(asignaturaId);

        assertNotNull(rendimiento.getAlumnos());
        assertTrue(rendimiento.getAlumnos().isEmpty());
        assertEquals(0, rendimiento.getCantidadEvaluaciones());
        assertEquals(0.0, rendimiento.getPromedioCurso(), 0.0001);
    }

    @Test
    public void rendimiento_agrupaPorAlumnoCorrectamente() {
        Long asignaturaId = 1L;

        Evaluacion a1 = new Evaluacion();
        a1.setAlumnoId(10L);
        a1.setAlumnoNombre("Ana");
        a1.setNota(7.0);

        Evaluacion a2 = new Evaluacion();
        a2.setAlumnoId(10L);
        a2.setAlumnoNombre("Ana");
        a2.setNota(5.0);

        Evaluacion b1 = new Evaluacion();
        b1.setAlumnoId(11L);
        b1.setAlumnoNombre("Bruno");
        b1.setNota(4.0);

        when(asigRepo.findById(asignaturaId)).thenReturn(Optional.empty());
        when(evalRepo.findByAsignaturaId(asignaturaId))
                .thenReturn(Arrays.asList(a1, a2, b1));

        RendimientoDTO rendimiento = queryService.obtenerRendimientoRico(asignaturaId);

        assertEquals(2, rendimiento.getAlumnos().size());
        RendimientoDTO.RendimientoAlumno ana = rendimiento.getAlumnos().get(0);
        assertEquals("Ana", ana.getAlumnoNombre());
        assertEquals(6.0, ana.getPromedio(), 0.0001);
        assertEquals(2, ana.getCantidadEvaluaciones());
    }

    @Test
    public void obtenerNotasAlumno_devuelveLasNotasMapeadasADto() {
        Evaluacion e = new Evaluacion();
        e.setId(1L);
        e.setAlumnoId(10L);
        e.setAlumnoNombre("Ana");
        e.setNombre("Control 1");
        e.setNota(5.8);

        when(evalRepo.findByAlumnoId(10L)).thenReturn(List.of(e));

        List<EvaluacionResponse> notas = queryService.obtenerNotasAlumno(10L);

        assertEquals(1, notas.size());
        assertEquals("Control 1", notas.get(0).getNombre());
        assertEquals(5.8, notas.get(0).getNota());
    }

    @Test
    public void toEvaluacionResponse_funcionaConAsignaturaNull() {
        Evaluacion e = new Evaluacion();
        e.setId(1L);
        e.setNota(4.5);

        EvaluacionResponse resp = queryService.toEvaluacionResponse(e);

        assertNull(resp.getAsignaturaId());
        assertEquals(4.5, resp.getNota());
    }

    @Test
    public void toEvaluacionResponse_incluyeElIdDeLaAsignaturaCuandoExiste() {
        Asignatura asig = new Asignatura();
        asig.setId(7L);
        asig.setNombreAsignatura("Inglés");

        Evaluacion e = new Evaluacion();
        e.setId(1L);
        e.setNota(6.0);
        e.setAsignatura(asig);

        EvaluacionResponse resp = queryService.toEvaluacionResponse(e);

        assertEquals(7L, resp.getAsignaturaId());
    }

    @Test
    public void obtenerCursoPorId_devuelveElCursoCuandoExiste() {
        Curso c = new Curso();
        c.setId(1L);
        c.setNombre("1º Medio A");
        c.setNivel("Educación Media");

        when(cursoRepo.findById(1L)).thenReturn(Optional.of(c));

        CursoDTO dto = queryService.obtenerCursoPorId(1L);

        assertEquals("1º Medio A", dto.getNombre());
    }

    @Test
    public void obtenerCursoPorId_devuelveNullSiNoExiste() {
        when(cursoRepo.findById(99L)).thenReturn(Optional.empty());
        assertNull(queryService.obtenerCursoPorId(99L));
    }

    @Test
    public void listarAsignaturas_mapeaElCursoAsociado() {
        Curso curso = new Curso();
        curso.setId(2L);
        curso.setNombre("2º Medio B");

        Asignatura a = new Asignatura();
        a.setId(1L);
        a.setNombreAsignatura("Historia");
        a.setCurso(curso);

        when(asigRepo.findAll()).thenReturn(List.of(a));

        List<cl.duoc.academic.dto.AsignaturaDTO> asignaturas = queryService.listarAsignaturas();

        assertEquals(1, asignaturas.size());
        assertEquals("Historia", asignaturas.get(0).getNombre());
        assertEquals(2L, asignaturas.get(0).getCursoId());
    }
}