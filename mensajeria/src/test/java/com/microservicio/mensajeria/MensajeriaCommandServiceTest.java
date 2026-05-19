package com.microservicio.mensajeria;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.model.EstadoMensaje;
import com.microservicio.mensajeria.model.Mensaje;
import com.microservicio.mensajeria.model.TipoMensaje;
import com.microservicio.mensajeria.repository.MensajeRepository;
import com.microservicio.mensajeria.service.MensajeMapper;
import com.microservicio.mensajeria.service.MensajeriaCommandService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MensajeriaCommandServiceTest {

    private final MensajeRepository repository = mock(MensajeRepository.class);
    private final MensajeMapper mapper = new MensajeMapper();
    private final MensajeriaCommandService service = new MensajeriaCommandService(repository, mapper);

    @Test
    void crearMensajeDirectoGuardaYRetornaDTO() {
        MensajeRequest request = MensajeRequest.builder()
                .titulo("Test")
                .contenido("Contenido")
                .remitenteId(1L)
                .remitenteNombre("Profesor")
                .remitenteRol("DOCENTE")
                .destinatarioId(2L)
                .destinatarioNombre("Apoderado")
                .destinatarioRol("APODERADO")
                .build();

        when(repository.save(any(Mensaje.class))).thenAnswer(invocation -> {
            Mensaje m = invocation.getArgument(0);
            m.setId(1L);
            m.setEstado(EstadoMensaje.ENVIADO);
            return m;
        });

        MensajeResponse response = service.crearMensaje(request, TipoMensaje.DIRECTO);

        assertNotNull(response);
        assertEquals(TipoMensaje.DIRECTO, response.getTipo());
        assertEquals(1L, response.getId());
        verify(repository, times(1)).save(any(Mensaje.class));
    }
}
