package com.microservicio.mensajeria;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.model.DestinatarioTipo;
import com.microservicio.mensajeria.model.EstadoMensaje;
import com.microservicio.mensajeria.model.Mensaje;
import com.microservicio.mensajeria.model.TipoMensaje;
import com.microservicio.mensajeria.repository.MensajeRepository;
import com.microservicio.mensajeria.service.MensajeService;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class MensajeServiceTest {

    private final MensajeRepository repository = mock(MensajeRepository.class);
    private final MensajeService service = new MensajeService(repository);

    @Test
    void crearMensaje_deberiaGuardarYRetornarDTO() {

        MensajeRequest request = MensajeRequest.builder()
                .titulo("Test")
                .contenido("Contenido")
                .remitenteId(1L)
                .remitenteNombre("Profesor")
                .destinatarioId(2L)
                .destinatarioNombre("Apoderado")
                .tipoMensaje(TipoMensaje.COMUNICADO_GENERAL)
                .destinatarioTipo(DestinatarioTipo.COMUNIDAD)
                .build();

        when(repository.save(any())).thenAnswer(invocation -> {
            Mensaje mensaje = invocation.getArgument(0);
            mensaje.setId(1L);
            return mensaje;
        });

        when(repository.findMensajeDTOById(1L)).thenReturn(
                new MensajeResponse(
                        1L,
                        "Test",
                        "Contenido",
                        "Profesor",
                        "Apoderado",
                        TipoMensaje.COMUNICADO_GENERAL,
                        DestinatarioTipo.COMUNIDAD,
                        EstadoMensaje.ENVIADO,
                        LocalDateTime.now()
                )
        );

        MensajeResponse response = service.crearMensaje(request);

        assertNotNull(response);
        verify(repository, times(1)).save(any());
        verify(repository, times(1)).findMensajeDTOById(1L);
    }
}