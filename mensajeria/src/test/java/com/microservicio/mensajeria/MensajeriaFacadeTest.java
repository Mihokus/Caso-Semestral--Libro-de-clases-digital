package com.microservicio.mensajeria;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.facade.MensajeriaFacade;
import com.microservicio.mensajeria.model.TipoMensaje;
import com.microservicio.mensajeria.service.MensajeriaCommandService;
import com.microservicio.mensajeria.service.MensajeriaQueryService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class MensajeriaFacadeTest {

    private final MensajeriaCommandService commandService = mock(MensajeriaCommandService.class);
    private final MensajeriaQueryService queryService = mock(MensajeriaQueryService.class);
    private final MensajeriaFacade facade = new MensajeriaFacade(commandService, queryService);

    @Test
    void publicarComunicadoFallaSinCursoId() {
        MensajeRequest request = MensajeRequest.builder()
                .titulo("Test")
                .contenido("Contenido")
                .remitenteId(1L)
                .remitenteNombre("Profesor")
                .remitenteRol("DOCENTE")
                .build();

        assertThrows(IllegalArgumentException.class, () -> facade.publicarComunicado(request));
        verifyNoInteractions(commandService);
    }

    @Test
    void publicarComunicadoDelegaAlCommandService() {
        MensajeRequest request = MensajeRequest.builder()
                .titulo("Test")
                .contenido("Contenido")
                .cursoId(10L)
                .remitenteId(1L)
                .remitenteNombre("Profesor")
                .remitenteRol("DOCENTE")
                .build();

        facade.publicarComunicado(request);

        verify(commandService, times(1)).crearMensaje(any(MensajeRequest.class), eq(TipoMensaje.COMUNICADO));
        verifyNoInteractions(queryService);
    }

    @Test
    void enviarDirectoFallaSinDestinatarioId() {
        MensajeRequest request = MensajeRequest.builder()
                .titulo("Test")
                .contenido("Contenido")
                .remitenteId(1L)
                .remitenteNombre("Profesor")
                .remitenteRol("DOCENTE")
                .build();

        assertThrows(IllegalArgumentException.class, () -> facade.enviarDirecto(request));
        verifyNoInteractions(commandService);
    }
}
