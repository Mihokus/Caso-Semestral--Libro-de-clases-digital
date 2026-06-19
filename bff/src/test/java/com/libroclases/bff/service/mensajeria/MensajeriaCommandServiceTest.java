package com.libroclases.bff.service.mensajeria;

import com.libroclases.bff.client.MensajeriaClient;
import com.libroclases.bff.client.dto.RawMensajeComunicadoRequest;
import com.libroclases.bff.client.dto.RawMensajeDirectoRequest;
import com.libroclases.bff.dto.MensajeComunicadoRequest;
import com.libroclases.bff.dto.MensajeDTO;
import com.libroclases.bff.dto.MensajeDirectoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MensajeriaCommandServiceTest {

    @Mock private MensajeriaClient client;
    @InjectMocks private MensajeriaCommandService service;

    @Test
    void enviarDirectoIncluyeAlRemitente() {
        MensajeDirectoRequest req = new MensajeDirectoRequest();
        req.setDestinatarioId(7L);
        req.setTitulo("Hola");
        req.setContenido("Mensaje");
        MensajeDTO expected = new MensajeDTO();
        when(client.enviarDirecto(any(RawMensajeDirectoRequest.class))).thenReturn(expected);

        MensajeDTO result = service.enviarDirecto(req, 5L, "Ana", "DOCENTE");

        assertThat(result).isSameAs(expected);
        ArgumentCaptor<RawMensajeDirectoRequest> captor = ArgumentCaptor.forClass(RawMensajeDirectoRequest.class);
        verify(client).enviarDirecto(captor.capture());
        assertThat(captor.getValue().getRemitenteId()).isEqualTo(5L);
    }

    @Test
    void enviarComunicadoDelegaEnElCliente() {
        MensajeComunicadoRequest req = new MensajeComunicadoRequest();
        req.setCursoId(1L);
        req.setTitulo("Aviso");
        req.setContenido("Contenido");
        MensajeDTO expected = new MensajeDTO();
        when(client.enviarComunicado(any(RawMensajeComunicadoRequest.class))).thenReturn(expected);

        assertThat(service.enviarComunicado(req, 5L, "Ana", "DOCENTE")).isSameAs(expected);
    }

    @Test
    void marcarLeidoDelegaEnElCliente() {
        MensajeDTO expected = new MensajeDTO();
        when(client.marcarLeido(3L)).thenReturn(expected);

        assertThat(service.marcarLeido(3L)).isSameAs(expected);
    }

    @Test
    void eliminarDelegaEnElCliente() {
        service.eliminar(3L);

        verify(client).eliminar(3L);
    }
}
