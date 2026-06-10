package com.libroclases.bff.service.mensajeria;

import com.libroclases.bff.client.MensajeriaClient;
import com.libroclases.bff.dto.MensajeDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MensajeriaQueryServiceTest {

    @Mock private MensajeriaClient client;
    @InjectMocks private MensajeriaQueryService service;

    @Test
    void inboxDelegaEnElCliente() {
        List<MensajeDTO> expected = List.of();
        when(client.inbox(1L)).thenReturn(expected);
        assertThat(service.inbox(1L)).isSameAs(expected);
    }

    @Test
    void enviadosDelegaEnElCliente() {
        List<MensajeDTO> expected = List.of();
        when(client.enviados(1L)).thenReturn(expected);
        assertThat(service.enviados(1L)).isSameAs(expected);
    }

    @Test
    void byIdDelegaEnElCliente() {
        MensajeDTO expected = new MensajeDTO();
        when(client.byId(9L)).thenReturn(expected);
        assertThat(service.byId(9L)).isSameAs(expected);
    }
}
