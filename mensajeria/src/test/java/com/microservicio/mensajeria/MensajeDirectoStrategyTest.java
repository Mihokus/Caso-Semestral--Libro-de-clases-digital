package com.microservicio.mensajeria;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.model.TipoMensaje;
import com.microservicio.mensajeria.strategy.MensajeDirectoStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MensajeDirectoStrategyTest {

    private final MensajeDirectoStrategy strategy = new MensajeDirectoStrategy();

    @Test
    void deberiaRetornarTipoDirecto() {
        assertEquals(TipoMensaje.DIRECTO, strategy.getTipoMensaje());
    }

    @Test
    void deberiaValidarMensajeDirectoCorrecto() {
        MensajeRequest request = requestValido();

        assertDoesNotThrow(() -> strategy.validar(request));
    }

    @Test
    void deberiaFallarSiNoTieneDestinatarioId() {
        MensajeRequest request = requestValido();
        request.setDestinatarioId(null);

        assertThrows(IllegalArgumentException.class,
                () -> strategy.validar(request));
    }

    @Test
    void deberiaFallarSiNoTieneDestinatarioNombre() {
        MensajeRequest request = requestValido();
        request.setDestinatarioNombre("");

        assertThrows(IllegalArgumentException.class,
                () -> strategy.validar(request));
    }

    @Test
    void deberiaFallarSiNoTieneDestinatarioRol() {
        MensajeRequest request = requestValido();
        request.setDestinatarioRol(null);

        assertThrows(IllegalArgumentException.class,
                () -> strategy.validar(request));
    }

    private MensajeRequest requestValido() {
        return MensajeRequest.builder()
                .titulo("Mensaje directo")
                .contenido("Contenido")
                .remitenteId(1L)
                .remitenteNombre("Profesor")
                .remitenteRol("DOCENTE")
                .destinatarioId(2L)
                .destinatarioNombre("Apoderado")
                .destinatarioRol("APODERADO")
                .build();
    }
}