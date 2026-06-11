package cl.colegio.ohiggins.asistencia.service.command;

import cl.colegio.ohiggins.asistencia.model.Asistencia;
import cl.colegio.ohiggins.asistencia.repository.AsistenciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaCommandServiceTest {

    @Mock
    private AsistenciaRepository asistenciaRepo;

    @InjectMocks
    private AsistenciaCommandService asistenciaCommandService;

    @Test
    void debeGuardarUnaAsistenciaConExito() {
        // 1. GIVEN
        Asistencia asistenciaEntrada = new Asistencia();
        asistenciaEntrada.setAlumnoId(1L);

        Asistencia asistenciaGuardada = new Asistencia();
        asistenciaGuardada.setId(50L);
        asistenciaGuardada.setAlumnoId(1L);

        when(asistenciaRepo.save(any(Asistencia.class))).thenReturn(asistenciaGuardada);

        // 2. WHEN
        Asistencia resultado = asistenciaCommandService.save(asistenciaEntrada);

        // 3. THEN
        assertNotNull(resultado);
        assertEquals(50L, resultado.getId());
        verify(asistenciaRepo, times(1)).save(asistenciaEntrada);
    }

    @Test
    void debeGuardarMultiplesAsistenciasConExito() {
        // 1. GIVEN
        Asistencia a1 = new Asistencia();
        Asistencia a2 = new Asistencia();
        List<Asistencia> listaEntrada = List.of(a1, a2);

        when(asistenciaRepo.saveAll(anyList())).thenReturn(listaEntrada);

        // 2. WHEN
        List<Asistencia> resultado = asistenciaCommandService.saveAll(listaEntrada);

        // 3. THEN
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(asistenciaRepo, times(1)).saveAll(listaEntrada);
    }
}