package cl.duoc.academic.facade;

import cl.duoc.academic.dto.RendimientoDTO;
import cl.duoc.academic.model.Asignatura;
import cl.duoc.academic.model.Evaluacion;
import cl.duoc.academic.repository.AsignaturaRepository;
import cl.duoc.academic.repository.CursoRepository;
import cl.duoc.academic.repository.EvaluacionRepository;
import cl.duoc.academic.service.AcademicaQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AcademicFacadeImplTest {

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
}
