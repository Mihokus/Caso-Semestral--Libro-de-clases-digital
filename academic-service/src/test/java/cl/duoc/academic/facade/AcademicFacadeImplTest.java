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

    @Mock private EvaluacionRepository evalRepo;
    @InjectMocks private AcademicFacadeImpl academicFacade;

    @Test
    public void testObtenerPromedioRendimiento(){
        Long id = 1L;
        Evaluacion e1 = new Evaluacion(); e1.setNota(6.0);
        Evaluacion e2 = new Evaluacion(); e2.setNota(4.0);
        when(evalRepo.findByAsignaturaId(id)).thenReturn(Arrays.asList(e1, e2));
        Double promedio = academicFacade.obtenerPromedioRendimiento(id);
        assertEquals(5.0, promedio, "El promedio debería ser 5.0");
    }
    public void testPromedioConNotasVacia() {
        when(evalRepo.findByAsignaturaId(1L)).thenReturn(Collections.emptyList());
        Double promedio = academicFacade.obtenerPromedioRendimiento(1L);
        assertEquals(0.0, promedio);
    }
}