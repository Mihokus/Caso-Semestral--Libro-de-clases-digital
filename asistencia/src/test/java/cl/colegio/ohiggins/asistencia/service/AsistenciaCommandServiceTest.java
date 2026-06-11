package cl.colegio.ohiggins.asistencia.service;

import cl.colegio.ohiggins.asistencia.model.Asistencia;
import cl.colegio.ohiggins.asistencia.repository.AsistenciaRepository;
import cl.colegio.ohiggins.asistencia.service.command.AsistenciaCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaCommandServiceTest {

    @Mock
    private AsistenciaRepository asistenciaRepository;

    @InjectMocks
    private AsistenciaCommandService service;

    @Test
    void debeGuardarAsistencia() {

        Asistencia asistencia = new Asistencia();

        when(asistenciaRepository.save(any(Asistencia.class)))
                .thenReturn(asistencia);

        Asistencia resultado = service.save(asistencia);

        assertEquals(asistencia, resultado);
        verify(asistenciaRepository).save(asistencia);
    }

    @Test
    void debeGuardarListaAsistencias() {

        List<Asistencia> lista =
                List.of(new Asistencia(), new Asistencia());

        when(asistenciaRepository.saveAll(lista))
                .thenReturn(lista);

        List<Asistencia> resultado =
                service.saveAll(lista);

        assertEquals(2, resultado.size());

        verify(asistenciaRepository)
                .saveAll(lista);
    }
}