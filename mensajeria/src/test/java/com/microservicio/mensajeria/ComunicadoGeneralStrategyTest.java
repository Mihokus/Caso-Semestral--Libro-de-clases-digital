package com.microservicio.mensajeria;

import com.microservicio.mensajeria.dto.MensajeRequest;
import com.microservicio.mensajeria.model.TipoMensaje;
import com.microservicio.mensajeria.strategy.ComunicadoGeneralStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComunicadoGeneralStrategyTest {

    private final ComunicadoGeneralStrategy strategy = new ComunicadoGeneralStrategy();

    @Test
    void deberiaRetornarTipoComunicado() {
        assertEquals(TipoMensaje.COMUNICADO, strategy.getTipoMensaje());
    }

    @Test
    void deberiaValidarComunicadoCorrecto() {
        MensajeRequest request = requestValido();

        assertDoesNotThrow(() -> strategy.validar(request));
    }

    @Test
    void deberiaFallarSiNoTieneCursoId() {
        MensajeRequest request = requestValido();
        request.setCursoId(null);

        assertThrows(IllegalArgumentException.class,
                () -> strategy.validar(request));
    }

    @Test
    void deberiaLimpiarDatosDeDestinatarioIndividual() {
        MensajeRequest request = requestValido();

        strategy.preparar(request);

        assertNull(request.getDestinatarioId());
        assertNull(request.getDestinatarioNombre());
        assertNull(request.getDestinatarioRol());
    }

    private MensajeRequest requestValido() {
        return MensajeRequest.builder()
                .titulo("Comunicado")
                .contenido("Contenido")
                .remitenteId(1L)
                .remitenteNombre("Inspectoría")
                .remitenteRol("ADMIN")
                .destinatarioId(2L)
                .destinatarioNombre("Apoderado")
                .destinatarioRol("APODERADO")
                .cursoId(10L)
                .build();
    }
}