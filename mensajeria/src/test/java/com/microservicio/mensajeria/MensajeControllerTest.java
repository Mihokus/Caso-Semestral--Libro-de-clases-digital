package com.microservicio.mensajeria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio.mensajeria.controller.MensajeController;
import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.dto.MensajeResponse;
import com.microservicio.mensajeria.dto.UsuarioResumenDTO;
import com.microservicio.mensajeria.model.TipoMensaje;
import com.microservicio.mensajeria.service.MensajeriaCommandService;
import com.microservicio.mensajeria.service.MensajeriaQueryService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MensajeControllerTest {

    private final MensajeriaCommandService commandService = mock(MensajeriaCommandService.class);
    private final MensajeriaQueryService queryService = mock(MensajeriaQueryService.class);

    private final MockMvc mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders
            .standaloneSetup(new MensajeController(commandService, queryService))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void crearMensajeDirecto_deberiaRetornarCreatedYUsarTipoDirecto() throws Exception {
        MensajeRequest request = requestBase();

        when(commandService.crearMensaje(any(MensajeRequest.class), eq(TipoMensaje.DIRECTO)))
                .thenReturn(responseDirecto());

        mockMvc.perform(post("/api/mensajes/directo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("DIRECTO"))
                .andExpect(jsonPath("$.titulo").value("Mensaje directo"));

        ArgumentCaptor<TipoMensaje> tipoCaptor = ArgumentCaptor.forClass(TipoMensaje.class);
        verify(commandService).crearMensaje(any(MensajeRequest.class), tipoCaptor.capture());
        assertEquals(TipoMensaje.DIRECTO, tipoCaptor.getValue());
    }

    @Test
    void crearComunicado_deberiaRetornarCreatedYUsarTipoComunicado() throws Exception {
        MensajeRequest request = requestBase();
        request.setCursoId(10L);

        when(commandService.crearMensaje(any(MensajeRequest.class), eq(TipoMensaje.COMUNICADO)))
                .thenReturn(responseComunicado());

        mockMvc.perform(post("/api/mensajes/comunicado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.tipo").value("COMUNICADO"));

        verify(commandService).crearMensaje(any(MensajeRequest.class), eq(TipoMensaje.COMUNICADO));
    }

    @Test
    void obtenerPorId_deberiaRetornarMensaje() throws Exception {
        when(queryService.obtenerPorId(1L)).thenReturn(responseDirecto());

        mockMvc.perform(get("/api/mensajes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("DIRECTO"));

        verify(queryService).obtenerPorId(1L);
    }

    @Test
    void obtenerInbox_deberiaRetornarListaDeMensajes() throws Exception {
        when(queryService.obtenerInbox(2L)).thenReturn(List.of(responseDirecto(), responseComunicado()));

        mockMvc.perform(get("/api/mensajes/inbox/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(queryService).obtenerInbox(2L);
    }

    @Test
    void marcarComoLeido_deberiaRetornarMensajeActualizado() throws Exception {
        when(commandService.marcarComoLeido(1L)).thenReturn(responseDirecto());

        mockMvc.perform(put("/api/mensajes/1/leido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(commandService).marcarComoLeido(1L);
    }

    @Test
    void eliminarMensaje_deberiaRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/api/mensajes/1"))
                .andExpect(status().isNoContent());

        verify(commandService).eliminarMensaje(1L);
    }

    private MensajeRequest requestBase() {
        return MensajeRequest.builder()
                .titulo("Mensaje directo")
                .contenido("Contenido")
                .remitenteId(1L)
                .remitenteNombre("Profesor")
                .remitenteRol("DOCENTE")
                .destinatarioId(2L)
                .destinatarioNombre("Apoderado")
                .destinatarioRol("APODERADO")
                .cursoId(10L)
                .build();
    }

    private MensajeResponse responseDirecto() {
        return new MensajeResponse(
                1L,
                TipoMensaje.DIRECTO,
                "Mensaje directo",
                "Contenido",
                new UsuarioResumenDTO(1L, "Profesor", "DOCENTE"),
                new UsuarioResumenDTO(2L, "Apoderado", "APODERADO"),
                10L,
                Instant.now(),
                false
        );
    }

    private MensajeResponse responseComunicado() {
        return new MensajeResponse(
                2L,
                TipoMensaje.COMUNICADO,
                "Comunicado",
                "Contenido comunicado",
                new UsuarioResumenDTO(1L, "Inspectoría", "ADMIN"),
                null,
                10L,
                Instant.now(),
                false
        );
    }
}