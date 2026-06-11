package cl.colegio.ohiggins.asistencia.repository;

import cl.colegio.ohiggins.asistencia.model.Anotacion;
import cl.colegio.ohiggins.asistencia.model.TipoAnotacion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnotacionRepositoryTest {

    @Mock
    private AnotacionRepository repository;

    @Test
    void debeBuscarAnotacionesAlumno() {
        Anotacion anotacion = new Anotacion();
        anotacion.setAlumnoId(1L);
        anotacion.setAlumnoNombre("Juan");

        when(repository.findByAlumnoIdOrderByFechaDesc(1L)).thenReturn(List.of(anotacion));

        List<Anotacion> resultado = repository.findByAlumnoIdOrderByFechaDesc(1L);

        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getAlumnoNombre());
    }
}