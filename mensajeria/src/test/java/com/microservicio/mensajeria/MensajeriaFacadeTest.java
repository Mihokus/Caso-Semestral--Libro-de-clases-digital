package com.microservicio.mensajeria;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.model.DestinatarioTipo;
import com.microservicio.mensajeria.model.TipoMensaje;
import com.microservicio.mensajeria.service.MensajeriaCommandService;
import com.microservicio.mensajeria.service.MensajeriaQueryService;
import com.microservicio.mensajeria.facade.MensajeriaFacade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MensajeriaFacadeTest {

    private final MensajeriaCommandService commandService = mock(MensajeriaCommandService.class);
    private final MensajeriaQueryService queryService = mock(MensajeriaQueryService.class);
    private final MensajeriaFacade facade = new MensajeriaFacade(commandService, queryService);

    @Test
    void publicarMensajeDifusion_deberiaFallarSiNoHayCursoId() {
        MensajeRequest request = MensajeRequest.builder()
                .titulo("Test")
                .contenido("Contenido")
                .remitenteId(1L)
                .remitenteNombre("Profesor")
                .remitenteRol("DOCENTE")
                .tipoMensaje(TipoMensaje.COMUNICADO_GENERAL)
                .destinatarioTipo(DestinatarioTipo.COMUNIDAD)
                .build();

        assertThrows(IllegalArgumentException.class, () -> facade.publicarMensajeDifusion(request));
        verifyNoInteractions(commandService);
    }

    @Test
    void publicarMensajeDifusion_deberiaDelegarAlCommandService() {
        MensajeRequest request = MensajeRequest.builder()
                .titulo("Test")
                .contenido("Contenido")
                .cursoId(10L)
                .remitenteId(1L)
                .remitenteNombre("Profesor")
                .remitenteRol("DOCENTE")
                .tipoMensaje(TipoMensaje.COMUNICADO_GENERAL)
                .destinatarioTipo(DestinatarioTipo.COMUNIDAD)
                .build();

        facade.publicarMensajeDifusion(request);

        verify(commandService, times(1)).crearMensaje(any(MensajeRequest.class));
        verifyNoInteractions(queryService);
    }
}