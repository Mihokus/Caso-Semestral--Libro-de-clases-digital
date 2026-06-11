package cl.colegio.ohiggins.asistencia.integration;

import cl.colegio.ohiggins.asistencia.repository.AlumnoRepository;
import cl.colegio.ohiggins.asistencia.repository.AnotacionRepository;
import cl.colegio.ohiggins.asistencia.repository.AsistenciaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AsistenciaIntegrationTest {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private AnotacionRepository anotacionRepository;

    @Test
    void contextoDebeCargarCorrectamente() {
        assertNotNull(alumnoRepository);
        assertNotNull(asistenciaRepository);
        assertNotNull(anotacionRepository);
    }

    @Test
    void debeConectarConBaseDatos() {
        long totalAlumnos = alumnoRepository.count();
        assertTrue(totalAlumnos >= 0);
    }
}