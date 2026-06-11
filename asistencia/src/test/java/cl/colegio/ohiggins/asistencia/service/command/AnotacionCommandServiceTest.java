package cl.colegio.ohiggins.asistencia.service.command;

import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.repository.AnotacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnotacionCommandServiceTest {

    @Mock
    private AnotacionRepository anotacionRepo;

    @InjectMocks
    private AnotacionCommandService anotacionCommandService;

    @Test
    void debeGuardarAnotacionConExito() {
        // 1. GIVEN (Configurar los datos de prueba y mocks)
        Anotacion anotacionEntrada = new Anotacion();
        anotacionEntrada.setAlumnoId(1L);
        anotacionEntrada.setDescripcion("Prueba de servicio");

        Anotacion anotacionGuardada = new Anotacion();
        anotacionGuardada.setId(100L); // Simula que la BD le asignó un ID
        anotacionGuardada.setAlumnoId(1L);
        anotacionGuardada.setDescripcion("Prueba de servicio");

        when(anotacionRepo.save(any(Anotacion.class))).thenReturn(anotacionGuardada);

        // 2. WHEN (Ejecutar el método del servicio)
        Anotacion resultado = anotacionCommandService.save(anotacionEntrada);

        // 3. THEN (Verificar que todo ande bien)
        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        assertEquals("Prueba de servicio", resultado.getDescripcion());

        // Verificar que el repositorio se llamó exactamente una vez
        verify(anotacionRepo, times(1)).save(anotacionEntrada);
    }
}
