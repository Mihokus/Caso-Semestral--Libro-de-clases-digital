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
class JustificadoStrategyTest {

    @Mock
    private AsistenciaRepository repo;

    @Mock
    private AsistenciaMapper mapper;

    @InjectMocks
    private JustificadoStrategy justificadoStrategy;

    @Test
    void debeEjecutarEstrategiaJustificadoConExito() {
        // 1. GIVEN (Configurar datos simulados de entrada con estado JUSTIFICADO)
        AsistenciaContext context = mock(AsistenciaContext.class);
        when(context.getAlumnoId()).thenReturn(3L);
        when(context.getCursoId()).thenReturn(10L);
        when(context.getFecha()).thenReturn(LocalDate.now());
        when(context.getEstado()).thenReturn(EstadoAsistencia.JUSTIFICADO);
        when(context.getRegistradoPorId()).thenReturn(100L);
        when(context.getRegistradoPorNombre()).thenReturn("Profesor Juan");

        Asistencia asistenciaGuardada = new Asistencia();
        asistenciaGuardada.setAlumnoId(3L);
        asistenciaGuardada.setEstado(EstadoAsistencia.JUSTIFICADO);
        when(repo.save(any(Asistencia.class))).thenReturn(asistenciaGuardada);

        AsistenciaDTO dtoEsperado = new AsistenciaDTO();
        dtoEsperado.setAlumnoId(3L);
        dtoEsperado.setEstado(EstadoAsistencia.JUSTIFICADO);
        when(mapper.toAsistenciaDTO(asistenciaGuardada)).thenReturn(dtoEsperado);

        // 2. WHEN (Ejecutar la lógica)
        AsistenciaDTO resultado = justificadoStrategy.ejecutar(context);

        // 3. THEN (Verificar que todo se guardó y mapeó bien)
        assertNotNull(resultado);
        assertEquals(3L, resultado.getAlumnoId());
        assertEquals(EstadoAsistencia.JUSTIFICADO, resultado.getEstado());

        verify(repo, times(1)).save(any(Asistencia.class));
        verify(mapper, times(1)).toAsistenciaDTO(asistenciaGuardada);
    }
}