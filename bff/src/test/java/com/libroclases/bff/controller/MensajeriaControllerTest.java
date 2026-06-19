package com.libroclases.bff.controller;

import com.libroclases.bff.dto.MensajeComunicadoRequest;
import com.libroclases.bff.dto.MensajeDTO;
import com.libroclases.bff.dto.MensajeDirectoRequest;
import com.libroclases.bff.service.mensajeria.MensajeriaCommandService;
import com.libroclases.bff.service.mensajeria.MensajeriaQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MensajeriaControllerTest {

    @Mock private MensajeriaQueryService queryService;
    @Mock private MensajeriaCommandService commandService;
    @InjectMocks private MensajeriaController controller;

    @Test
    void inboxDelega() {
        List<MensajeDTO> expected = List.of();
        when(queryService.inbox(1L)).thenReturn(expected);
        assertThat(controller.inbox(1L)).isSameAs(expected);
    }

    @Test
    void enviadosDelega() {
        List<MensajeDTO> expected = List.of();
        when(queryService.enviados(1L)).thenReturn(expected);
        assertThat(controller.enviados(1L)).isSameAs(expected);
    }

    @Test
    void byIdDelega() {
        MensajeDTO expected = new MensajeDTO();
        when(queryService.byId(9L)).thenReturn(expected);
        assertThat(controller.byId(9L)).isSameAs(expected);
    }

    @Test
    void enviarDirectoDevuelve201() {
        MensajeDirectoRequest req = new MensajeDirectoRequest();
        MensajeDTO dto = new MensajeDTO();
        when(commandService.enviarDirecto(req, 5L, "Ana", "DOCENTE")).thenReturn(dto);

        ResponseEntity<MensajeDTO> res = controller.enviarDirecto(req, 5L, "Ana", "DOCENTE");

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void enviarComunicadoDevuelve201() {
        MensajeComunicadoRequest req = new MensajeComunicadoRequest();
        MensajeDTO dto = new MensajeDTO();
        when(commandService.enviarComunicado(req, 5L, "Ana", "DOCENTE")).thenReturn(dto);

        ResponseEntity<MensajeDTO> res = controller.enviarComunicado(req, 5L, "Ana", "DOCENTE");

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void marcarLeidoDelega() {
        MensajeDTO expected = new MensajeDTO();
        when(commandService.marcarLeido(3L)).thenReturn(expected);
        assertThat(controller.marcarLeido(3L)).isSameAs(expected);
    }

    @Test
    void eliminarDevuelve204() {
        ResponseEntity<Void> res = controller.eliminar(3L);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(commandService).eliminar(3L);
    }
}
