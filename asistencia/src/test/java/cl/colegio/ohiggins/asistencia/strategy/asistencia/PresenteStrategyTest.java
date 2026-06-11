package cl.colegio.ohiggins.asistencia.strategy.asistencia;

import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.model.Asistencia;
import cl.colegio.ohiggins.asistencia.model.EstadoAsistencia;
import cl.colegio.ohiggins.asistencia.repository.AsistenciaRepository;
import cl.colegio.ohiggins.asistencia.service.AsistenciaMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PresenteStrategyTest {

    @Mock
    private AsistenciaRepository repo;

    @Mock
    private AsistenciaMapper mapper;

    @InjectMocks
    private PresenteStrategy presenteStrategy;

    @Test
    void debeEjecutarEstrategiaPresenteConExito() {
        // 1. GIVEN (Preparar los datos de entrada)
        AsistenciaContext context = mock(AsistenciaContext.class);
        when(context.getAlumnoId()).thenReturn(1L);
        when(context.getCursoId()).thenReturn(10L);
        when(context.getFecha()).thenReturn(LocalDate.now());
        when(context.getEstado()).thenReturn(EstadoAsistencia.PRESENTE);
        when(context.getRegistradoPorId()).thenReturn(100L);
        when(context.getRegistradoPorNombre()).thenReturn("Profesor Juan");

        Asistencia asistenciaGuardada = new Asistencia();
        when(repo.save(any(Asistencia.class))).thenReturn(asistenciaGuardada);

        AsistenciaDTO dtoEsperado = new AsistenciaDTO();
        dtoEsperado.setAlumnoId(1L);
        dtoEsperado.setEstado(EstadoAsistencia.PRESENTE);
        when(mapper.toAsistenciaDTO(asistenciaGuardada)).thenReturn(dtoEsperado);

        // 2. WHEN (Ejecutar el test)
        AsistenciaDTO resultado = presenteStrategy.ejecutar(context);

        // 3. THEN (Verificar resultados)
        assertNotNull(resultado);
        assertEquals(1L, resultado.getAlumnoId());
        assertEquals(EstadoAsistencia.PRESENTE, resultado.getEstado());

        verify(repo, times(1)).save(any(Asistencia.class));
        verify(mapper, times(1)).toAsistenciaDTO(asistenciaGuardada);
    }
}