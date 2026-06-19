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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NegativaStrategyTest {

    @Mock
    private AnotacionRepository repo;

    @Mock
    private AsistenciaMapper mapper;

    @InjectMocks
    private NegativaStrategy negativaStrategy;

    @Test
    void debeGuardarAnotacionNegativaSinAlertaCuandoTienePocas() {
        // 1. GIVEN
        AnotacionContext context = mock(AnotacionContext.class);

        // Mockeamos el objeto request de forma dinámica usando respuestas encadenadas (Deep Stubs)
        // Así Mockito simula context.getRequest().getDescripcion() sin obligarnos a importar la clase exacta
        AnotacionContext contextMock = mock(AnotacionContext.class, Mockito.RETURNS_DEEP_STUBS);
        when(contextMock.getAlumnoId()).thenReturn(1L);
        when(contextMock.getRequest().getDescripcion()).thenReturn("Falta de respeto");
        when(contextMock.getRequest().getRegistradoPorId()).thenReturn(100L);
        when(contextMock.getRequest().getRegistradoPorNombre()).thenReturn("Profesor Inspector");

        Anotacion anotacionGuardada = new Anotacion();
        when(repo.save(any(Anotacion.class))).thenReturn(anotacionGuardada);

        // Solo 1 anotación previa (menos de 3)
        Anotacion historica1 = new Anotacion();
        historica1.setTipo(TipoAnotacion.NEGATIVA);
        when(repo.findByAlumnoIdOrderByFechaDesc(1L)).thenReturn(List.of(historica1));

        AnotacionDTO dtoEsperado = new AnotacionDTO();
        when(mapper.toAnotacionDTO(anotacionGuardada)).thenReturn(dtoEsperado);

        // 2. WHEN
        AnotacionDTO resultado = negativaStrategy.ejecutar(contextMock);

        // 3. THEN
        assertNotNull(resultado);
        verify(repo, times(1)).save(any(Anotacion.class));
    }

    @Test
    void debeGuardarAnotacionYCrearAlertaCuandoAcumulaTresOMas() {
        // 1. GIVEN
        AnotacionContext contextMock = mock(AnotacionContext.class, Mockito.RETURNS_DEEP_STUBS);
        when(contextMock.getAlumnoId()).thenReturn(1L);
        when(contextMock.getRequest().getDescripcion()).thenReturn("Nueva falta grave");
        when(contextMock.getRequest().getRegistradoPorId()).thenReturn(100L);
        when(contextMock.getRequest().getRegistradoPorNombre()).thenReturn("Profesor Inspector");

        Anotacion anotacionGuardada = new Anotacion();
        when(repo.save(any(Anotacion.class))).thenReturn(anotacionGuardada);

        // El alumno ya acumula 3 anotaciones negativas previas
        Anotacion h1 = new Anotacion(); h1.setTipo(TipoAnotacion.NEGATIVA);
        Anotacion h2 = new Anotacion(); h2.setTipo(TipoAnotacion.NEGATIVA);
        Anotacion h3 = new Anotacion(); h3.setTipo(TipoAnotacion.NEGATIVA);
        when(repo.findByAlumnoIdOrderByFechaDesc(1L)).thenReturn(List.of(h1, h2, h3));

        AnotacionDTO dtoEsperado = new AnotacionDTO();
        when(mapper.toAnotacionDTO(anotacionGuardada)).thenReturn(dtoEsperado);

        // 2. WHEN
        AnotacionDTO resultado = negativaStrategy.ejecutar(contextMock);

        // 3. THEN
        assertNotNull(resultado);
        // Verificamos las dos inserciones en la base de datos (la negativa + la alerta neutral)
        verify(repo, times(2)).save(any(Anotacion.class));
    }
}