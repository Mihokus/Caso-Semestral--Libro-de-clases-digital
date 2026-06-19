package cl.colegio.ohiggins.asistencia.service;

import cl.colegio.ohiggins.asistencia.dto.AsistenciaDTO;
import cl.colegio.ohiggins.asistencia.repository.AsistenciaRepository;
import cl.colegio.ohiggins.asistencia.service.query.AsistenciaQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AsistenciaQueryServiceTest {

    @Mock
    private AsistenciaRepository asistenciaRepository;

    @Mock
    private AsistenciaMapper mapper;

    @InjectMocks
    private AsistenciaQueryService service;

    @Test
    void contextoCargaCorrectamente() {

        assertNotNull(service);
    }
}