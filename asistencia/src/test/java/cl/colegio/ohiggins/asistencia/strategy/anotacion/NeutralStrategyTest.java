package cl.colegio.ohiggins.asistencia.strategy.anotacion;

import cl.colegio.ohiggins.asistencia.dto.AnotacionDTO;
import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.model.TipoAnotacion;
import cl.colegio.ohiggins.asistencia.repository.AnotacionRepository;
import cl.colegio.ohiggins.asistencia.service.AsistenciaMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NeutralStrategyTest {

    @Mock
    private AnotacionRepository repo;

    @Mock
    private AsistenciaMapper mapper;

    @InjectMocks
    private NeutralStrategy neutralStrategy;

    @Test
    void debeEjecutarEstrategiaNeutralConExito() {
        // 1. GIVEN (Configurar contexto con Deep Stubs)
        AnotacionContext contextMock = mock(AnotacionContext.class, Mockito.RETURNS_DEEP_STUBS);
        when(contextMock.getAlumnoId()).thenReturn(10L);
        when(contextMock.getRequest().getTipo()).thenReturn(TipoAnotacion.NEUTRAL);
        when(contextMock.getRequest().getDescripcion()).thenReturn("Cambio de puesto solicitado por apoderado");
        when(contextMock.getRequest().getRegistradoPorId()).thenReturn(300L);
        when(contextMock.getRequest().getRegistradoPorNombre()).thenReturn("Profesor Jefe");

        Anotacion anotacionGuardada = new Anotacion();
        anotacionGuardada.setAlumnoId(10L);
        anotacionGuardada.setTipo(TipoAnotacion.NEUTRAL);
        when(repo.save(any(Anotacion.class))).thenReturn(anotacionGuardada);

        AnotacionDTO dtoEsperado = new AnotacionDTO();
        when(mapper.toAnotacionDTO(anotacionGuardada)).thenReturn(dtoEsperado);

        // 2. WHEN (Ejecutar la estrategia neutral)
        AnotacionDTO resultado = neutralStrategy.ejecutar(contextMock);

        // 3. THEN (Verificaciones)
        assertNotNull(resultado);
        verify(repo, times(1)).save(any(Anotacion.class));
        verify(mapper, times(1)).toAnotacionDTO(anotacionGuardada);
    }
}