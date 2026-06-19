package cl.colegio.ohiggins.asistencia.strategy.asistencia;

import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.model.Asistencia;
import cl.colegio.ohiggins.asistencia.model.EstadoAsistencia;
import cl.colegio.ohiggins.asistencia.repository.AnotacionRepository;
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
class AusenteStrategyTest {

    @Mock
    private AsistenciaRepository asistenciaRepo;

    @Mock
    private AnotacionRepository anotacionRepo;

    @Mock
    private AsistenciaMapper mapper;

    @InjectMocks
    private AusenteStrategy ausenteStrategy;

    @Test
    void debeEjecutarEstrategiaAusenteYCrearAnotacionAutomatica() {
        // 1. GIVEN (Configurar los datos simulados de entrada)
        AsistenciaContext context = mock(AsistenciaContext.class);
        when(context.getAlumnoId()).thenReturn(5L);
        when(context.getCursoId()).thenReturn(12L);
        when(context.getFecha()).thenReturn(LocalDate.now());
        when(context.getEstado()).thenReturn(EstadoAsistencia.AUSENTE);
        when(context.getRegistradoPorId()).thenReturn(200L);
        when(context.getRegistradoPorNombre()).thenReturn("Profesor Carlos");

        // Simular el guardado de la asistencia
        Asistencia asistenciaGuardada = new Asistencia();
        asistenciaGuardada.setAlumnoId(5L);
        asistenciaGuardada.setEstado(EstadoAsistencia.AUSENTE);
        when(asistenciaRepo.save(any(Asistencia.class))).thenReturn(asistenciaGuardada);

        // Simular el guardado de la anotación automática
        Anotacion anotacionGuardada = new Anotacion();
        when(anotacionRepo.save(any(Anotacion.class))).thenReturn(anotacionGuardada);

        // Simular la conversión al DTO
        AsistenciaDTO dtoEsperado = new AsistenciaDTO();
        dtoEsperado.setAlumnoId(5L);
        dtoEsperado.setEstado(EstadoAsistencia.AUSENTE);
        when(mapper.toAsistenciaDTO(asistenciaGuardada)).thenReturn(dtoEsperado);

        // 2. WHEN (Ejecutar el método bajo prueba)
        AsistenciaDTO resultado = ausenteStrategy.ejecutar(context);

        // 3. THEN (Verificaciones de negocio)
        assertNotNull(resultado);
        assertEquals(5L, resultado.getAlumnoId());
        assertEquals(EstadoAsistencia.AUSENTE, resultado.getEstado());

        // Verificamos que se hayan llamado ambos repositorios exactamente 1 vez
        verify(asistenciaRepo, times(1)).save(any(Asistencia.class));
        verify(anotacionRepo, times(1)).save(any(Anotacion.class));
        verify(mapper, times(1)).toAsistenciaDTO(asistenciaGuardada);
    }
}