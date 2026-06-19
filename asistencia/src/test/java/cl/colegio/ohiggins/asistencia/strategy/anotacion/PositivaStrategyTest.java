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
class PositivaStrategyTest {

    @Mock
    private AnotacionRepository repo;

    @Mock
    private AsistenciaMapper mapper;

    @InjectMocks
    private PositivaStrategy positivaStrategy;

    @Test
    void debeEjecutarEstrategiaPositivaConExito() {
        // 1. GIVEN (Configurar contexto simulado)
        AnotacionContext contextMock = mock(AnotacionContext.class, Mockito.RETURNS_DEEP_STUBS);
        when(contextMock.getAlumnoId()).thenReturn(15L);
        when(contextMock.getRequest().getTipo()).thenReturn(TipoAnotacion.POSITIVA);
        when(contextMock.getRequest().getDescripcion()).thenReturn("Excelente participación en clases de matemáticas");
        when(contextMock.getRequest().getRegistradoPorId()).thenReturn(400L);
        when(contextMock.getRequest().getRegistradoPorNombre()).thenReturn("Profesora Ana");

        Anotacion anotacionGuardada = new Anotacion();
        anotacionGuardada.setAlumnoId(15L);
        anotacionGuardada.setTipo(TipoAnotacion.POSITIVA);
        when(repo.save(any(Anotacion.class))).thenReturn(anotacionGuardada);

        AnotacionDTO dtoEsperado = new AnotacionDTO();
        when(mapper.toAnotacionDTO(anotacionGuardada)).thenReturn(dtoEsperado);

        // 2. WHEN (Ejecutar la lógica)
        AnotacionDTO resultado = positivaStrategy.ejecutar(contextMock);

        // 3. THEN (Verificaciones de ejecución)
        assertNotNull(resultado);
        verify(repo, times(1)).save(any(Anotacion.class));
        verify(mapper, times(1)).toAnotacionDTO(anotacionGuardada);
    }
}