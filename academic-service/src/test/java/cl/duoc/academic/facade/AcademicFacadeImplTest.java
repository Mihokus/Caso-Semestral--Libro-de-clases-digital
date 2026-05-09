package cl.duoc.academic.facade;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import cl.duoc.academic.model.*;
import cl.duoc.academic.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class AcademicFacadeImplTest {

    @Mock private AcademicaQueryService queryService;
    @Mock private AcademicaCommandService commandService;

    @InjectMocks
    private AcademicFacadeImpl academicFacade;

    @Test
    public void testObtenerRendimientoTotal(){

        Long id = 1L;
        RendimientoDTO mockDto = new RendimientoDTO(5.5, 3, "Aprobado");
        when(queryService.obtenerRendimientoRico(id)).thenReturn(mockDto);

        RendimientoDTO resultado = academicFacade.obtenerRendimientoTotal(id);

        assertNotNull(resultado);
        assertEquals(5.5, resultado.getPromedio());
        verify(queryService, times(1)).obtenerRendimientoRico(id);
    }
}