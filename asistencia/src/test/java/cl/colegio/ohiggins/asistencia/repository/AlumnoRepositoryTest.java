package cl.colegio.ohiggins.asistencia.repository;

import cl.colegio.ohiggins.asistencia.entity.Alumno;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlumnoRepositoryTest {

    @Mock
    private AlumnoRepository alumnoRepository;

    @Test
    void debeGuardarAlumno() {
        Alumno alumno = new Alumno();
        alumno.setId(1L);
        alumno.setNombre("Juan Perez");

        when(alumnoRepository.save(any(Alumno.class))).thenReturn(alumno);

        Alumno guardado = alumnoRepository.save(alumno);

        assertNotNull(guardado);
        assertNotNull(guardado.getId());
        assertEquals("Juan Perez", guardado.getNombre());
    }

    @Test
    void debeBuscarAlumnoPorId() {
        Alumno alumno = new Alumno();
        alumno.setId(1L);
        alumno.setNombre("Pedro Soto");

        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));

        var encontrado = alumnoRepository.findById(1L);

        assertTrue(encontrado.isPresent());
        assertEquals("Pedro Soto", encontrado.get().getNombre());
    }
}