package cl.colegio.ohiggins.asistencia.repository;

import cl.colegio.ohiggins.asistencia.model.Asistencia;
import cl.colegio.ohiggins.asistencia.model.EstadoAsistencia;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsistenciaRepositoryTest {

    @Mock
    private AsistenciaRepository repository;

    @Test
    void debeBuscarPorAlumno() {
        Asistencia asistencia = new Asistencia();
        asistencia.setAlumnoId(1L);
        asistencia.setAlumnoNombre("Juan");

        when(repository.findByAlumnoIdOrderByFechaDesc(1L)).thenReturn(List.of(asistencia));

        List<Asistencia> resultado = repository.findByAlumnoIdOrderByFechaDesc(1L);

        assertFalse(resultado.isEmpty());
        assertEquals(1L, resultado.get(0).getAlumnoId());
    }

    @Test
    void debeBuscarPorCursoYFecha() {
        LocalDate fecha = LocalDate.now();
        Asistencia asistencia = new Asistencia();
        asistencia.setCursoId(10L);
        asistencia.setFecha(fecha);

        when(repository.findByCursoIdAndFecha(10L, fecha)).thenReturn(List.of(asistencia));

        List<Asistencia> resultado = repository.findByCursoIdAndFecha(10L, fecha);

        assertEquals(1, resultado.size());
    }
}