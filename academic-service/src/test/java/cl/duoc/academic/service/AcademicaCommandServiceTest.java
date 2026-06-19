package cl.duoc.academic.service;

import cl.duoc.academic.dto.EvaluacionResponse;
import cl.duoc.academic.model.Asignatura;
import cl.duoc.academic.model.Curso;
import cl.duoc.academic.dto.AsignaturaDTO;
import cl.duoc.academic.dto.CursoDTO;
import cl.duoc.academic.dto.CursoRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import cl.duoc.academic.dto.AsignaturaRequest;
import cl.duoc.academic.dto.EvaluacionDTO;
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
    @Test
    public void guardarCurso_creaElCursoYDevuelveElDto() {
        CursoRequest req = new CursoRequest();
        req.setNombre("3º Medio A");
        req.setNivel("Educación Media");

        when(cursoRepo.save(any(Curso.class))).thenAnswer(inv -> {
            Curso c = inv.getArgument(0);
            c.setId(5L);
            return c;
        });
        when(queryService.toCursoDTO(any(Curso.class))).thenAnswer(inv -> {
            Curso c = inv.getArgument(0);
            return new CursoDTO(c.getId(), c.getNombre(), c.getNivel(), 0);
        });

        CursoDTO dto = commandService.guardarCurso(req);

        assertEquals(5L, dto.getId());
        assertEquals("3º Medio A", dto.getNombre());
        verify(cursoRepo).save(any(Curso.class));
    }

    @Test
    public void guardarAsignatura_asociaElCursoCuandoVieneCursoId() {
        Curso curso = new Curso();
        curso.setId(2L);
        curso.setNombre("1º Medio B");

        AsignaturaRequest req = new AsignaturaRequest();
        req.setNombre("Historia");
        req.setCursoId(2L);
        req.setDocenteNombre("Pedro Rojas");

        when(cursoRepo.findById(2L)).thenReturn(Optional.of(curso));
        when(asigRepo.save(any(Asignatura.class))).thenAnswer(inv -> {
            Asignatura a = inv.getArgument(0);
            a.setId(3L);
            return a;
        });
        when(queryService.toAsignaturaDTO(any(Asignatura.class))).thenAnswer(inv -> {
            Asignatura a = inv.getArgument(0);
            Long cursoId = a.getCurso() != null ? a.getCurso().getId() : null;
            return new AsignaturaDTO(a.getId(), a.getNombreAsignatura(), cursoId,
                    null, a.getDocenteId(), a.getDocenteNombre());
        });

        AsignaturaDTO dto = commandService.guardarAsignatura(req);

        assertEquals(3L, dto.getId());
        assertEquals(2L, dto.getCursoId());
        assertEquals("Pedro Rojas", dto.getDocenteNombre());
    }

    @Test
    public void guardarAsignatura_sinCursoIdNoConsultaElRepositorioDeCursos() {
        AsignaturaRequest req = new AsignaturaRequest();
        req.setNombre("Taller Electivo");

        when(asigRepo.save(any(Asignatura.class))).thenAnswer(inv -> inv.getArgument(0));
        when(queryService.toAsignaturaDTO(any(Asignatura.class)))
                .thenReturn(new AsignaturaDTO(null, "Taller Electivo", null, null, null, null));

        AsignaturaDTO dto = commandService.guardarAsignatura(req);

        assertEquals("Taller Electivo", dto.getNombre());
        verify(cursoRepo, never()).findById(any());
    }
}